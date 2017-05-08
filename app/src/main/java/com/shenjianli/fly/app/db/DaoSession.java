package com.shenjianli.fly.app.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.shenjianli.fly.model.LocationEntity;
import com.shenjianli.fly.model.LocEntity;

import com.shenjianli.fly.app.db.LocationEntityDao;
import com.shenjianli.fly.app.db.LocEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig locationEntityDaoConfig;
    private final DaoConfig locEntityDaoConfig;

    private final LocationEntityDao locationEntityDao;
    private final LocEntityDao locEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        locationEntityDaoConfig = daoConfigMap.get(LocationEntityDao.class).clone();
        locationEntityDaoConfig.initIdentityScope(type);

        locEntityDaoConfig = daoConfigMap.get(LocEntityDao.class).clone();
        locEntityDaoConfig.initIdentityScope(type);

        locationEntityDao = new LocationEntityDao(locationEntityDaoConfig, this);
        locEntityDao = new LocEntityDao(locEntityDaoConfig, this);

        registerDao(LocationEntity.class, locationEntityDao);
        registerDao(LocEntity.class, locEntityDao);
    }
    
    public void clear() {
        locationEntityDaoConfig.clearIdentityScope();
        locEntityDaoConfig.clearIdentityScope();
    }

    public LocationEntityDao getLocationEntityDao() {
        return locationEntityDao;
    }

    public LocEntityDao getLocEntityDao() {
        return locEntityDao;
    }

}