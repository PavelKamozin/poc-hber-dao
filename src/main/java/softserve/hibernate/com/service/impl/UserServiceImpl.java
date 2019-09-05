/*Copyright (c) 2019-2020 softserveinc.com All Rights Reserved.
 This software is the confidential and proprietary information of softserveinc.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with softserveinc.com*/
package softserve.hibernate.com.service.impl;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softserve.hibernate.com.dao.GenericDao;
import softserve.hibernate.com.entity.User;
import softserve.hibernate.com.service.UserService;
import softserve.hibernate.com.service.util.ServiceUtil;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * ServiceImpl object for domain model class User.
 *
 * @see User
 */
@Service("UserService")
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    @Qualifier("UserDao")
    private GenericDao<User, Integer> userDao;

    @Autowired
    private ObjectMapper objectMapper;

    public void setUserDao(GenericDao<User, Integer> userDao) {
        this.userDao = userDao;
    }

    @Transactional
    @Override
    public User create(User users) {
        LOGGER.debug("Creating a new User with information: {}", users);

        User usersCreated = this.userDao.create(users);
        // reloading object from database to get database defined & server defined values.
        return this.userDao.refresh(usersCreated);
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(Integer usersId) {
        LOGGER.debug("Finding User by id: {}", usersId);
        return this.userDao.findById(usersId);
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Integer usersId) {
        LOGGER.debug("Finding User by id: {}", usersId);
        try {
            return this.userDao.findById(usersId);
        } catch (EntityNotFoundException ex) {
            LOGGER.debug("No User found with id: {}", usersId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findByMultipleIds(List<Integer> usersIds, boolean orderedReturn) {
        LOGGER.debug("Finding User by ids: {}", usersIds);

        return this.userDao.findByMultipleIds(usersIds, orderedReturn);
    }

    @Override
    public User findByUniqueKey(User user) throws IllegalAccessException {
        if (Objects.isNull(user)) {
            String message = "Can't proceed with empty User object";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
        LOGGER.debug("Finding User by map : {}", user);
        return this.userDao.findByUniqueKey(user);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    @Override
    public User update(User users) {
        LOGGER.debug("Updating User with information: {}", users);

        this.userDao.update(users);
        this.userDao.refresh(users);

        return users;
    }

    @Transactional
    @Override
    public User partialUpdate(Integer usersId, User user) {
        LOGGER.debug("Partially Updating the User with id: {}", usersId);
        User existingUser = getById(usersId);
        ServiceUtil.copyNonNullProperties(user,existingUser);
        existingUser = update(existingUser);
        return existingUser;
    }

    @Transactional
    @Override
    public User delete(Integer usersId) {
        LOGGER.debug("Deleting User with id: {}", usersId);
        User deleted = this.userDao.findById(usersId);
        if (deleted == null) {
            LOGGER.debug("No User found with id: {}", usersId);
            throw new EntityNotFoundException("User is not found for " + usersId);
        }
        this.userDao.delete(deleted);
        return deleted;
    }

    @Transactional
    @Override
    public void delete(User users) {
        LOGGER.debug("Deleting User with {}", users);
        this.userDao.delete(users);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all User");
        return this.userDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all User");
        return this.userDao.searchByQuery(query, pageable);
    }

    /*
    @Transactional(readOnly = true, timeout = 300)
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service db_hibernate for table User to {} format", exportType);
        return this.userDao.export(exportType, query, pageable);
    }

    @Transactional(readOnly = true, timeout = 300)
    @Override
    public void export(DataExportOptions options, Pageable pageable, OutputStream outputStream) {
        LOGGER.debug("exporting data in the service db_hibernate for table User to {} format", options.getExportType());
        this.userDao.export(options, pageable, outputStream);
    }
    */

    @Transactional(readOnly = true)
    @Override
    public long count(String query) {
        return this.userDao.count(query);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Map<String, Object>> getAggregatedValues(AggregationInfo aggregationInfo, Pageable pageable)
        throws IllegalAccessException {
        return this.userDao.getAggregatedValues(aggregationInfo, pageable);
    }


}