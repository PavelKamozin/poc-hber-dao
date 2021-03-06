/*Copyright (c) 2019-2020 softserveinc.com All Rights Reserved.
 This software is the confidential and proprietary information of softserveinc.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with softserveinc.com*/
package softserve.hibernate.com.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import softserve.hibernate.com.entity.Role;

import javax.persistence.EntityNotFoundException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Service object for domain model class {@link Role}.
 */
public interface RoleService {

    /**
     * Creates a new Role. It does cascade insert for all the children in a single transaction.
     *
     * This method overrides the input field values using Server side or database managed properties defined on Role if any.
     *
     * @param role Details of the Role to be created; value cannot be null.
     * @return The newly created Role.
     */
    Role create(Role role);


	/**
     * Returns Role by given id if exists.
     *
     * @param rolesId The id of the Role to get; value cannot be null.
     * @return Role associated with the given rolesId.
	 * @throws EntityNotFoundException If no Role is found.
     */
    Role getById(Integer rolesId);

    /**
     * Find and return the Role by given id if exists, returns null otherwise.
     *
     * @param rolesId The id of the Role to get; value cannot be null.
     * @return Role associated with the given rolesId.
     */
    Role findById(Integer rolesId);

	/**
     * Find and return the list of Role by given id's.
     *
     * If orderedReturn true, the return List is ordered and positional relative to the incoming ids.
     *
     * In case of unknown entities:
     *
     * If enabled, A null is inserted into the List at the proper position(s).
     * If disabled, the nulls are not put into the return List.
     *
     * @param rolesIds The id's of the Role to get; value cannot be null.
     * @param orderedReturn Should the return List be ordered and positional in relation to the incoming ids?
     * @return Role associated with the given rolesIds.
     */
    List<Role> findByMultipleIds(List<Integer> rolesIds, boolean orderedReturn);

    /**
     * Find and return the Role by given map of keys and values.
     *
     * If orderedReturn true, the return List is ordered and positional relative to the incoming ids.
     *
     * In case of unknown entities:
     *
     * If enabled, A null is inserted into the List at the proper position(s).
     * If disabled, the nulls are not put into the return List.
     *
     * @param fieldValueMap The id's of the Role to get; value cannot be null.
     * @return User associated with the given map of values.
     */
    Role findByUniqueKey(Role role) throws IllegalAccessException;


    /**
     * Updates the details of an existing Role. It replaces all fields of the existing Role with the given role.
     *
     * This method overrides the input field values using Server side or database managed properties defined on Role if any.
     *
     * @param role The details of the Role to be updated; value cannot be null.
     * @return The updated Role.
     * @throws EntityNotFoundException if no Role is found with given input.
     */
    Role update(Role role);


    /**
     * Partially updates the details of an existing Role. It updates only the
     * fields of the existing Role which are passed in the rolesPatch.
     *
     * This method overrides the input field values using Server side or database managed properties defined on Role if any.
     *
     * @param rolesId The id of the Role to be deleted; value cannot be null.
     * @param role The partial data of Role which is supposed to be updated; value cannot be null.
     * @return The updated Role.
     * @throws EntityNotFoundException if no Role is found with given input.
     */
    Role partialUpdate(Integer rolesId, Role role);

    /**
     * Deletes an existing Role with the given id.
     *
     * @param rolesId The id of the Role to be deleted; value cannot be null.
     * @return The deleted Role.
     * @throws EntityNotFoundException if no Role found with the given id.
     */
    Role delete(Integer rolesId);

    /**
     * Deletes an existing Role with the given object.
     *
     * @param role The instance of the Role to be deleted; value cannot be null.
     */
    void delete(Role role);

    /**
     * Find all Role matching the given QueryFilter(s).
     * All the QueryFilter(s) are ANDed to filter the results.
     * This method returns Paginated results.
     *
     * @deprecated Use {@link #findAll(String, Pageable)} instead.
     *
     * @param queryFilters Array of queryFilters to filter the results; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null returns all matching records.
     * @return Paginated list of matching Role.
     *
     * @see QueryFilter
     * @see Pageable
     * @see Page
     */
    @Deprecated
    Page<Role> findAll(QueryFilter[] queryFilters, Pageable pageable);

    /**
     * Find all Role matching the given input query. This method returns Paginated results.
     * Note: Go through the documentation for <u>query</u> syntax.
     *
     * @param query The query to filter the results; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null returns all matching records.
     * @return Paginated list of matching Role.
     *
     * @see Pageable
     * @see Page
     */
    Page<Role> findAll(String query, Pageable pageable);

    /**
     * Exports all Role matching the given input query to the given exportType format.
     * Note: Go through the documentation for <u>query</u> syntax.
     *
     * @param exportType The format in which to export the data; value cannot be null.
     * @param query The query to filter the results; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null exports all matching records.
     * @return The Downloadable file in given export type.
     *
     * @see Pageable
     * @see ExportType
     * @see Downloadable

    Downloadable export(ExportType exportType, String query, Pageable pageable);
     */

    /**
     * Exports all Role matching the given input query to the given exportType format.
     *
     * @param options The export options provided by the user; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null exports all matching records.
     * @param outputStream The output stream of the file for the exported data to be written to.
     *
     * @see DataExportOptions
     * @see Pageable
     * @see OutputStream

    void export(DataExportOptions options, Pageable pageable, OutputStream outputStream);
     */

    /**
     * Retrieve the count of the Role in the repository with matching query.
     * Note: Go through the documentation for <u>query</u> syntax.
     *
     * @param query query to filter results. No filters applied if the input is null/empty.
     * @return The count of the Role.
     */
    long count(String query);

    /**
     * Retrieve aggregated values with matching aggregation info.
     *
     * @param aggregationInfo info related to aggregations.
     * @param pageable Details of the pagination information along with the sorting options. If null exports all matching records.
     * @return Paginated data with included fields.
     *
     * @see AggregationInfo
     * @see Pageable
     * @see Page
	 */
    Page<Map<String, Object>> getAggregatedValues(AggregationInfo aggregationInfo, Pageable pageable) throws IllegalAccessException;
}