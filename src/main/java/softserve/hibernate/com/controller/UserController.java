package softserve.hibernate.com.controller;

import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import softserve.hibernate.com.entity.User;
import softserve.hibernate.com.service.UserService;

import java.util.List;
import java.util.Map;

import static softserve.hibernate.com.config.Constants.Controller.DEFAULT_PAGE_SIZE;
import static softserve.hibernate.com.config.Constants.Controller.DEFAULT_START_PAGE;

/**
 * Controller object for domain model class User.
 *
 * @see User
 */
@RestController("UserController")
@Api(value = "UserController", tags = "Exposes APIs to work with User resource.")
@RequestMapping("/db_hibernate/User")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    @Qualifier("UserService")
    private UserService userService;

    @ApiOperation(value = "Creates a new User instance.")
    @RequestMapping(method = RequestMethod.POST)
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public User createUser(@RequestBody User user) {
        LOGGER.debug("Create User with information: {}", user);

        user = userService.create(user);
        LOGGER.debug("Created User with information: {}", user);

        return user;
    }

    @ApiOperation(value = "Returns the User instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public User getUser(@PathVariable("id") Integer id) {
        LOGGER.debug("Getting User with id: {}", id);

        User foundUser = userService.getById(id);
        LOGGER.debug("User details with id: {}", foundUser);

        return foundUser;
    }

    @ApiOperation(value = "Updates the User instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PUT)
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public User editUser(@PathVariable("id") Integer id, @RequestBody User user) {
        LOGGER.debug("Editing User with id: {}", user.getId());

        user.setId(id);
        user = userService.update(user);
        LOGGER.debug("User details with id: {}", user);

        return user;
    }

    @ApiOperation(value = "Partially updates the User instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public User patchUser(@PathVariable("id") Integer id, @RequestBody User user) {
        LOGGER.debug("Partially updating User with id: {}", id);

        User patchedUser = userService.partialUpdate(id, user);
        LOGGER.debug("User details after partial update: {}", patchedUser);

        return patchedUser;
    }

    @ApiOperation(value = "Deletes the User instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.DELETE)
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public boolean deleteUser(@PathVariable("id") Integer id) {
        LOGGER.debug("Deleting User with id: {}", id);

        User deletedUser = userService.delete(id);

        return deletedUser != null;
    }

    /**
     * @deprecated Use {@link #findUser(String, int, int)} instead.
     */
    @Deprecated
    @ApiOperation(value = "Returns the list of User instances matching the search criteria.")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    //@XssDisable
    public Page<User> searchUserByQueryFilters(@RequestParam(defaultValue = DEFAULT_START_PAGE, required = false) int pageNumber,
                                               @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                               @RequestBody QueryFilter[] queryFilters) {
        LOGGER.debug("Rendering User list by query filter:{}", (Object) queryFilters);
        return userService.findAll(queryFilters, PageRequest.of(pageNumber, pageSize));
    }

    @ApiOperation(value = "Returns the paginated list of User instances matching the optional query (q) request param. " +
            "If there is no query provided, it returns all the instances. Pagination & Sorting parameters such as page& size, " +
            "sort can be sent as request parameters. The sort value should be a comma separated list of field names " +
            "& optional sort order to sort the data on. eg: field1 asc, field2 desc etc ")
    @RequestMapping(method = RequestMethod.GET)
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<User> findUser(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query,
                               @RequestParam(defaultValue = DEFAULT_START_PAGE, required = false) int pageNumber,
                               @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        LOGGER.debug("Rendering User list by filter: {}", query);
        return userService.findAll(query, PageRequest.of(pageNumber, pageSize));
    }

    @ApiOperation(value = "Returns the paginated list of User instances matching the optional query (q) request param. " +
            "This API should be used only if the query string is too big to fit in GET request with request param. " +
            "The request has to made in application/x-www-form-urlencoded format.")
    @RequestMapping(value = "/filter", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    //@XssDisable
    public Page<User> filterUser(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query,
                                 @RequestParam(defaultValue = DEFAULT_START_PAGE, required = false) int pageNumber,
                                 @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        LOGGER.debug("Rendering User list by filter {}", query);
        return userService.findAll(query, PageRequest.of(pageNumber, pageSize));
    }

/*
    @ApiOperation(value = "Returns downloadable file for the data matching the optional query (q) request param.
    If query string is too big to fit in GET request's query param, use POST method with application/x-www-form-urlencoded format.")
    @RequestMapping(value = "/export/{exportType}", method = {RequestMethod.GET,  RequestMethod.POST}, produces = "application/octet-stream")
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    //@XssDisable
    public Downloadable exportUser(@PathVariable("exportType") ExportType exportType, @ApiParam("conditions to filter the results")
    @RequestParam(value = "q", required = false) String query, Pageable pageable) {
         return userService.export(exportType, query, pageable);
    }

    @ApiOperation(value = "Returns a URL to download a file for the data matching the optional query (q) request param
    and the required fields provided in the Export Options.")
    @RequestMapping(value = "/export", method = {RequestMethod.POST}, consumes = "application/json")
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    //@XssDisable
    public StringWrapper exportUserAndGetURL(@RequestBody DataExportOptions exportOptions, Pageable pageable) {
        String exportedFileName = exportOptions.getFileName();
        if(exportedFileName == null || exportedFileName.isEmpty()) {
            exportedFileName = User.class.getSimpleName();
        }
        exportedFileName += exportOptions.getExportType().getExtension();
        String exportedUrl = exportedFileManager.registerAndGetURL(exportedFileName, outputStream -> userService.export(exportOptions, pageable, outputStream));
        return new StringWrapper(exportedUrl);
    }
*/

    @ApiOperation(value = "Returns the total count of User instances matching the optional query (q) request param. " +
            "If query string is too big to fit in GET request's query param, use POST method with application/x-www-form-urlencoded format.")
    @RequestMapping(value = "/count", method = {RequestMethod.GET, RequestMethod.POST})
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    //@XssDisable
    public Long countUser(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query) {
        LOGGER.debug("counting User");
        return userService.count(query);
    }

    @ApiOperation(value = "Returns aggregated result with given aggregation info")
    @RequestMapping(value = "/aggregations", method = RequestMethod.POST)
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    //@XssDisable
    public Page<Map<String, Object>> getUserAggregatedValues(@RequestBody AggregationInfo aggregationInfo,
                                                             @RequestParam(defaultValue = DEFAULT_START_PAGE, required = false) int pageNumber,
                                                             @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize) throws IllegalAccessException {
        if (pageNumber < 1) {
            throw new IllegalArgumentException("Unexpected error {" + pageNumber + "},please check server logs for more information");
        }
        LOGGER.debug("Fetching aggregated results for {}", aggregationInfo);
        return userService.getAggregatedValues(aggregationInfo, PageRequest.of(pageNumber - 1, pageSize));
    }

    @ApiOperation(value = "Returns users by multiple ids")
    @RequestMapping(value = "/findByMultipleIds", method = RequestMethod.POST)
    public List<User> findByMultipleIds(@RequestBody List<Integer> usersIds,
                                        @ApiParam("conditions to order the results")
                                        @RequestParam(value = "q", required = false)
                                                boolean orderedReturn) {
        LOGGER.debug("Rendering User list by list userIds {}", usersIds);
        return userService.findByMultipleIds(usersIds, orderedReturn);
    }


    @ApiOperation(value = "Returns user by map of unique keys")
    @RequestMapping(value = "/findByUniqueKey", method = RequestMethod.POST)
    public User findByUniqueKey(@RequestBody User user) throws IllegalAccessException {
        LOGGER.debug("Rendering User by user object {}", user);
        return userService.findByUniqueKey(user);
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service userService instance
     */
    protected void setUserService(UserService service) {
        this.userService = service;
    }
}