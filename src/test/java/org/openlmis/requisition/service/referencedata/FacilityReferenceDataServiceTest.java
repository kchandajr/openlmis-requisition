/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.requisition.service.referencedata;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.openlmis.requisition.dto.FacilityDto;
import org.openlmis.requisition.dto.MinimalFacilityDto;
import org.openlmis.requisition.service.BaseCommunicationService;

public class FacilityReferenceDataServiceTest extends BaseReferenceDataServiceTest<FacilityDto> {

  FacilityReferenceDataService service;

  @Override
  protected FacilityDto generateInstance() {
    return new FacilityDto();
  }

  @Override
  protected BaseCommunicationService<FacilityDto> getService() {
    return new FacilityReferenceDataService();
  }

  @Override
  @Before
  public void setUp() {
    super.setUp();
    service = (FacilityReferenceDataService) prepareService();
  }

  @Test
  public void shouldFindFacilitiesByIds() {
    // given
    UUID facility1 = UUID.randomUUID();
    UUID facility2 = UUID.randomUUID();


    // when
    FacilityDto dto = mockArrayResponseEntityAndGetDto();
    List<FacilityDto> result = service.search(Sets.newHashSet(facility1, facility2));

    // then
    assertThat(result, hasSize(1));
    assertTrue(result.contains(dto));

    verifyArrayRequest()
        .isGetRequest()
        .hasAuthHeader()
        .hasEmptyBody();
  }

  @Test
  public void shouldFindByParameters() {
    // given
    String code = "code";
    String name = "name";
    UUID zoneId = UUID.randomUUID();
    boolean recurse = true;

    Map<String, Object> expectedBody = new HashMap<>();
    expectedBody.put("code", code);
    expectedBody.put("name", name);
    expectedBody.put("recurse", recurse);
    expectedBody.put("zoneId", zoneId);

    MinimalFacilityDto dto = new MinimalFacilityDto();

    // when
    mockPageResponseEntity(dto);
    List<MinimalFacilityDto> result = service.search(code, name, zoneId, recurse);

    // then
    assertThat(result, hasSize(1));
    assertTrue(result.contains(dto));

    verifyPageRequest()
        .isPostRequest()
        .hasAuthHeader()
        .hasBody(expectedBody);
  }

  @Test
  public void shouldSearchForSupplyingDepots() {
    // given
    UUID program = UUID.randomUUID();
    UUID supervisoryNode = UUID.randomUUID();

    // when
    FacilityDto dto = mockArrayResponseEntityAndGetDto();
    List<FacilityDto> result = service
        .searchSupplyingDepots(program, supervisoryNode);

    // then
    assertThat(result, hasSize(1));
    assertTrue(result.contains(dto));

    verifyArrayRequest()
        .isGetRequest()
        .hasAuthHeader()
        .hasEmptyBody()
        .hasQueryParameter("programId", program)
        .hasQueryParameter("supervisoryNodeId", supervisoryNode);
  }
}