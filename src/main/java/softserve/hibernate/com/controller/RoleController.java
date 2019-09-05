/*Copyright (c) 2019-2020 softserveinc.com All Rights Reserved.
 This software is the confidential and proprietary information of softserveinc.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with softserveinc.com*/
package softserve.hibernate.com.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import softserve.hibernate.com.entity.Role;
import softserve.hibernate.com.service.RoleService;

import java.util.List;
import java.util.Map;

import static softserve.hibernate.com.config.Constants.Controller.DEFAULT_PAGE_SIZE;
import static softserve.hibernate.com.config.Constants.Controller.DEFAULT_START_PAGE;

/**
 * Controller object for domain model class Role.
 *
 * @see Role
 */
@RestController("RoleController")
@Api(value = "RoleController", tags = "Exposes APIs to work with Role resource.")
@RequestMapping("/db_hibernate/Role")
public class RoleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "Creates a new Role instance.")
    @RequestMapping(method = RequestMethod.POST)
    ////@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Role createRole(@RequestBody Role roles) {
        LOGGER.debug("Create Role with information: {}", roles);

        roles = roleService.create(roles);
        LOGGER.debug("Created Role with information: {}", roles);

        return roles;
    }

    @ApiOperation(value = "Returns the Role instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    ////@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Role getRole(@PathVariable("id") Integer id) {
        LOGGER.debug("Getting Role with id: {}", id);

        Role foundRole = roleService.getById(id);
        LOGGER.debug("Role details with id: {}", foundRole);

        return foundRole;
    }

    @ApiOperation(value = "Updates the Role instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PUT)
    ////@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Role editRole(@PathVariable("id") Integer id, @RequestBody Role roles) {
        LOGGER.debug("Editing Role with id: {}", roles.getId());

        roles.setId(id);
        roles = roleService.update(roles);
        LOGGER.debug("Role details with id: {}", roles);

        return roles;
    }

    @ApiOperation(value = "Partially updates the Role instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PATCH)
    ////@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Role patchRole(@PathVariable("id") Integer id, @RequestBody Role role) {
        LOGGER.debug("Partially updating Role with id: {}", id);

        Role roles = roleService.partialUpdate(id, role);
        LOGGER.debug("Role details after partial update: {}", roles);

        return roles;
    }

    @ApiOperation(value = "Deletes the Role instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.DELETE)
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public boolean deleteRole(@PathVariable("id") Integer id) {
        LOGGER.debug("Deleting Role with id: {}", id);

        Role deletedRole = roleService.delete(id);

        return deletedRole != null;
    }

    /**
     * @deprecated Use {@link #findRole(String, int, int)} instead.
     */
    @Deprecated
    @ApiOperation(value = "Returns the list of Role instances matching the search criteria.")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    ////@XssDisable
    public Page<Role> searchRoleByQueryFilters(@RequestParam(defaultValue = DEFAULT_START_PAGE, required = false) int pageNumber,
                                               @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                               @RequestBody QueryFilter[] queryFilters) {
        LOGGER.debug("Rendering Role list by query filter: {}", (Object) queryFilters);
        return roleService.findAll(queryFilters, PageRequest.of(pageNumber, pageSize));
    }

    @ApiOperation(value = "Returns the paginated list of Role instances matching the optional query (q) request param. " +
            "If there is no query provided, it returns all the instances. Pagination & Sorting parameters such as page& size, " +
            "sort can be sent as request parameters. The sort value should be a comma separated list of field names " +
            "& optional sort order to sort the data on. eg: field1 asc, field2 desc etc ")
    @RequestMapping(method = RequestMethod.GET)
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Role> findRole(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query,
                               @RequestParam(defaultValue = DEFAULT_START_PAGE, required = false) int pageNumber,
                               @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        LOGGER.debug("Rendering Role list by filter: {}", query);
        return roleService.findAll(query, PageRequest.of(pageNumber, pageSize));
    }

    @ApiOperation(value = "Returns the paginated list of Role instances matching the optional query (q) request param." +
            " This API should be used only if the query string is too big to fit in GET request with request param. " +
            "The request has to made in application/x-www-form-urlencoded format.")
    @RequestMapping(value = "/filter", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    //@XssDisable
    public Page<Role> filterRole(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query,
                                 @RequestParam(defaultValue = DEFAULT_START_PAGE, required = false) int pageNumber,
                                 @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        LOGGER.debug("Rendering Role list by filter {}", query);
        return roleService.findAll(query, PageRequest.of(pageNumber, pageSize));
    }

        /*
    @ApiOperation(value = "Returns downloadable file for the data matching the optional query (q) request param. If query string is too big to fit in GET request's query param, use POST method with application/x-www-form-urlencoded format.")
    @RequestMapping(value = "/export/{exportType}", method = {RequestMethod.GET,  RequestMethod.POST}, produces = "application/octet-stream")
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    //@XssDisable
    public Downloadable exportRole(@PathVariable("exportType") ExportType exportType, @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
         return roleService.export(exportType, query, pageable);
    }


    @ApiOperation(value = "Returns a URL to download a file for the data matching the optional query (q) request param and the required fields provided in the Export Options.") 
    @RequestMapping(value = "/export", method = {RequestMethod.POST}, consumes = "application/json")
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    //@XssDisable
    public StringWrapper exportRoleAndGetURL(@RequestBody DataExportOptions exportOptions, Pageable pageable) {
        String exportedFileName = exportOptions.getFileName();
        if(exportedFileName == null || exportedFileName.isEmpty()) {
            exportedFileName = Role.class.getSimpleName();
        }
        exportedFileName += exportOptions.getExportType().getExtension();
        String exportedUrl = exportedFileManager.registerAndGetURL(exportedFileName, outputStream -> roleService.export(exportOptions, pageable, outputStream));
        return new StringWrapper(exportedUrl);
    }
     */

    @ApiOperation(value = "Returns the total count of Role instances matching the optional query (q) request param. " +
            "If query string is too big to fit in GET request's query param, use POST method with application/x-www-form-urlencoded format.")
    @RequestMapping(value = "/count", method = {RequestMethod.GET, RequestMethod.POST})
    //@WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    //@XssDisable
    public Long countRole(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query) {
        LOGGER.debug("counting Role");
        return roleService.count(query);
    }

    @ApiOperation(value = "Returns aggregated result with given aggregation info")
	@RequestMapping(value = "/aggregations", method = RequestMethod.POST)
	//@XssDisable
	public Page<Map<String, Object>> getRoleAggregatedValues(@RequestBody AggregationInfo aggregationInfo,
                                                             @RequestParam(defaultValue = DEFAULT_START_PAGE, required = false) int pageNumber,
                                                             @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize)
            throws IllegalAccessException {
        LOGGER.debug("Fetching aggregated results for {}", aggregationInfo);
        return roleService.getAggregatedValues(aggregationInfo, PageRequest.of(pageNumber, pageSize));
    }


    @ApiOperation(value = "Returns users by multiple ids")
    @RequestMapping(value = "/findByMultipleIds", method = RequestMethod.POST)
    public List<Role> findByMultipleIds(@RequestBody List<Integer> roleIds,
                                        @ApiParam("conditions to order the results")
                                        @RequestParam(value = "q", required = false)
                                                boolean orderedReturn) {
        LOGGER.debug("Rendering User list by list userIds {}", roleIds);
        return roleService.findByMultipleIds(roleIds, orderedReturn);
    }


    @ApiOperation(value = "Returns user by map of unique keys")
    @RequestMapping(value = "/findByUniqueKey", method = RequestMethod.POST)
    public Role findByUniqueKey(@RequestBody Role role) throws IllegalAccessException {
        LOGGER.debug("Rendering Role by object {}", role);
        return roleService.findByUniqueKey(role);
    }
    /**
     * This setter method should only be used by unit tests
     *
     * @param service RoleService instance
     */
    protected void setRoleService(RoleService service) {
        this.roleService = service;
    }

}