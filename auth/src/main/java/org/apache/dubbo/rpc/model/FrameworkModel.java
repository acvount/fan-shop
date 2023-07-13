package org.apache.dubbo.rpc.model;

import org.apache.dubbo.common.config.Environment;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.extension.ExtensionScope;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.resource.GlobalResourcesRepository;
import org.apache.dubbo.common.utils.Assert;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.metadata.definition.TypeDefinitionBuilder;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


/**
 * @author : acfan
 * date : create in 2023/7/10 20:51
 * description : 类重写，解决热加载报错问题(Nacos 配置热加载)
 **/
public class FrameworkModel extends ScopeModel {
    protected static final Logger LOGGER = LoggerFactory.getLogger(FrameworkModel.class);
    public static final String NAME = "FrameworkModel";
    private static final AtomicLong index = new AtomicLong(1L);
    private static final Object globalLock = new Object();
    private static volatile FrameworkModel defaultInstance;
    private static final List<FrameworkModel> allInstances = new CopyOnWriteArrayList();
    private final AtomicLong appIndex = new AtomicLong(0L);
    private volatile ApplicationModel defaultAppModel;
    private final List<ApplicationModel> applicationModels = new CopyOnWriteArrayList();
    private final List<ApplicationModel> pubApplicationModels = new CopyOnWriteArrayList();
    private final FrameworkServiceRepository serviceRepository;
    private final ApplicationModel internalApplicationModel;
    private final ReentrantLock destroyLock = new ReentrantLock();

    public FrameworkModel() {
        super((ScopeModel)null, ExtensionScope.FRAMEWORK, false);
        synchronized(globalLock) {
            synchronized(this.instLock) {
                this.setInternalId(String.valueOf(index.getAndIncrement()));
                allInstances.add(this);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(this.getDesc() + " is created");
                }

                this.initialize();
                TypeDefinitionBuilder.initBuilders(this);
                this.serviceRepository = new FrameworkServiceRepository(this);
                ExtensionLoader<ScopeModelInitializer> initializerExtensionLoader = this.getExtensionLoader(ScopeModelInitializer.class);
                Set<ScopeModelInitializer> initializers = initializerExtensionLoader.getSupportedExtensionInstances();
                Iterator var5 = initializers.iterator();

                while(var5.hasNext()) {
                    ScopeModelInitializer initializer = (ScopeModelInitializer)var5.next();
                    initializer.initializeFrameworkModel(this);
                }

                this.internalApplicationModel = new ApplicationModel(this, true);
                this.internalApplicationModel.getApplicationConfigManager().setApplication(new ApplicationConfig(this.internalApplicationModel, "DUBBO_INTERNAL_APPLICATION"));
                this.internalApplicationModel.setModelName("DUBBO_INTERNAL_APPLICATION");
            }
        }
    }

    protected void onDestroy() {
        synchronized(this.instLock) {
            if (defaultInstance == this && LOGGER.isInfoEnabled()) {
                LOGGER.info("Destroying default framework model: " + this.getDesc());
            }

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(this.getDesc() + " is destroying ...");
            }

            Iterator var2 = (new ArrayList(this.applicationModels)).iterator();

            while(var2.hasNext()) {
                ApplicationModel applicationModel = (ApplicationModel)var2.next();
                applicationModel.destroy();
            }

            this.checkApplicationDestroy();
            this.notifyDestroy();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(this.getDesc() + " is destroyed");
            }

            synchronized(globalLock) {
                allInstances.remove(this);
                resetDefaultFrameworkModel();
            }

            this.destroyGlobalResources();
        }
    }

    private void checkApplicationDestroy() {
        synchronized(this.instLock) {
            if (this.applicationModels.size() > 0) {
                List<String> remainApplications = (List)this.applicationModels.stream().map(ScopeModel::getDesc).collect(Collectors.toList());
                throw new IllegalStateException("Not all application models are completely destroyed, remaining " + remainApplications.size() + " application models may be created during destruction: " + remainApplications);
            }
        }
    }

    private void destroyGlobalResources() {
        synchronized(globalLock) {
            if (allInstances.isEmpty()) {
                GlobalResourcesRepository.getInstance().destroy();
            }

        }
    }

    public static FrameworkModel defaultModel() {
        FrameworkModel instance = defaultInstance;
        if (instance == null) {
            synchronized(globalLock) {
                resetDefaultFrameworkModel();
                if (defaultInstance == null) {
                    defaultInstance = new FrameworkModel();
                }

                instance = defaultInstance;
            }
        }

        Assert.notNull(instance, "Default FrameworkModel is null");
        return instance;
    }

    public static List<FrameworkModel> getAllInstances() {
        synchronized(globalLock) {
            return Collections.unmodifiableList(new ArrayList(allInstances));
        }
    }

    public static void destroyAll() {
        synchronized(globalLock) {
            Iterator var1 = (new ArrayList(allInstances)).iterator();

            while(var1.hasNext()) {
                FrameworkModel frameworkModel = (FrameworkModel)var1.next();
                frameworkModel.destroy();
            }

        }
    }

    public ApplicationModel newApplication() {
        synchronized(this.instLock) {
            return new ApplicationModel(this);
        }
    }

    public ApplicationModel defaultApplication() {
        ApplicationModel appModel = this.defaultAppModel;
        if (appModel == null) {
            this.checkDestroyed();
            this.resetDefaultAppModel();
            if ((appModel = this.defaultAppModel) == null) {
                synchronized(this.instLock) {
                    if (this.defaultAppModel == null) {
                        this.defaultAppModel = this.newApplication();
                    }

                    appModel = this.defaultAppModel;
                }
            }
        }

        Assert.notNull(appModel, "Default ApplicationModel is null");
        return appModel;
    }

    ApplicationModel getDefaultAppModel() {
        return this.defaultAppModel;
    }

    void addApplication(ApplicationModel applicationModel) {
        this.checkDestroyed();
        synchronized(this.instLock) {
            if (!this.applicationModels.contains(applicationModel)) {
                applicationModel.setInternalId(this.buildInternalId(this.getInternalId(), this.appIndex.getAndIncrement()));
                this.applicationModels.add(applicationModel);
                if (!applicationModel.isInternal()) {
                    this.pubApplicationModels.add(applicationModel);
                }
            }

        }
    }

    void removeApplication(ApplicationModel model) {
        synchronized(this.instLock) {
            this.applicationModels.remove(model);
            if (!model.isInternal()) {
                this.pubApplicationModels.remove(model);
            }

            this.resetDefaultAppModel();
        }
    }

    void tryDestroyProtocols() {
        synchronized(this.instLock) {
            if (this.pubApplicationModels.size() == 0) {
                this.notifyProtocolDestroy();
            }

        }
    }

    void tryDestroy() {
        synchronized(this.instLock) {
            if (this.pubApplicationModels.size() == 0) {
                this.destroy();
            }

        }
    }

    private void checkDestroyed() {
        if (this.isDestroyed()) {
            throw new IllegalStateException("FrameworkModel is destroyed");
        }
    }

    private void resetDefaultAppModel() {
        synchronized(this.instLock) {
            if (this.defaultAppModel == null || this.defaultAppModel.isDestroyed()) {
                ApplicationModel oldDefaultAppModel = this.defaultAppModel;
                if (this.pubApplicationModels.size() > 0) {
                    this.defaultAppModel = (ApplicationModel)this.pubApplicationModels.get(0);
                } else {
                    this.defaultAppModel = null;
                }

                if (defaultInstance == this && oldDefaultAppModel != this.defaultAppModel && LOGGER.isInfoEnabled()) {
                    LOGGER.info("Reset global default application from " + safeGetModelDesc(oldDefaultAppModel) + " to " + safeGetModelDesc(this.defaultAppModel));
                }

            }
        }
    }

    private static void resetDefaultFrameworkModel() {
        synchronized(globalLock) {
            if (defaultInstance == null || defaultInstance.isDestroyed()) {
                FrameworkModel oldDefaultFrameworkModel = defaultInstance;
                if (allInstances.size() > 0) {
                    defaultInstance = (FrameworkModel)allInstances.get(0);
                } else {
                    defaultInstance = null;
                }

                if (oldDefaultFrameworkModel != defaultInstance && LOGGER.isInfoEnabled()) {
                    LOGGER.info("Reset global default framework from " + safeGetModelDesc(oldDefaultFrameworkModel) + " to " + safeGetModelDesc(defaultInstance));
                }

            }
        }
    }

    private static String safeGetModelDesc(ScopeModel scopeModel) {
        return scopeModel != null ? scopeModel.getDesc() : null;
    }

    public List<ApplicationModel> getApplicationModels() {
        synchronized(globalLock) {
            return Collections.unmodifiableList(this.pubApplicationModels);
        }
    }

    public List<ApplicationModel> getAllApplicationModels() {
        synchronized(globalLock) {
            return Collections.unmodifiableList(this.applicationModels);
        }
    }

    public ApplicationModel getInternalApplicationModel() {
        return this.internalApplicationModel;
    }

    public FrameworkServiceRepository getServiceRepository() {
        return this.serviceRepository;
    }

    protected Lock acquireDestroyLock() {
        return this.destroyLock;
    }

    /***
     * 报错关键位置
     * @return defaultEnvironment
     */
    public Environment getModelEnvironment() {
//        throw new UnsupportedOperationException("Environment is inaccessible for FrameworkModel");
        return defaultAppModel.getModelEnvironment();
    }

    protected boolean checkIfClassLoaderCanRemoved(ClassLoader classLoader) {
        return super.checkIfClassLoaderCanRemoved(classLoader) && this.applicationModels.stream().noneMatch((applicationModel) -> {
            return applicationModel.containsClassLoader(classLoader);
        });
    }
}
