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

package org.openlmis.requisition.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openlmis.requisition.domain.Requisition;
import org.openlmis.requisition.domain.RequisitionStatus;
import org.openlmis.requisition.dto.BasicRequisitionDto;
import org.openlmis.requisition.dto.FacilityDto;
import org.openlmis.requisition.dto.ProcessingPeriodDto;
import org.openlmis.requisition.dto.ProgramDto;
import org.openlmis.requisition.service.PeriodService;
import org.openlmis.requisition.service.referencedata.FacilityReferenceDataService;
import org.openlmis.requisition.service.referencedata.ProgramReferenceDataService;
import java.time.ZonedDateTime;
import java.util.UUID;

public class BasicRequisitionDtoBuilderTest {

  @Mock
  private FacilityReferenceDataService facilityReferenceDataService;

  @Mock
  private PeriodService periodService;

  @Mock
  private ProgramReferenceDataService programReferenceDataService;

  @Mock
  private FacilityDto facilityDto;

  @Mock
  private ProcessingPeriodDto processingPeriodDto;

  @Mock
  private ProgramDto programDto;

  @InjectMocks
  private BasicRequisitionDtoBuilder basicRequisitionDtoBuilder = new BasicRequisitionDtoBuilder();

  private Requisition requisition;

  private UUID requisitionUuid = UUID.randomUUID();
  private UUID facilityUuid = UUID.randomUUID();
  private UUID processingPeriodUuid = UUID.randomUUID();
  private UUID programUuid = UUID.randomUUID();
  private UUID supervisoryNodeUuid = UUID.randomUUID();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    requisition = buildRequisition();
  }

  @Test
  public void shouldBuildDtoFromRequisition() {
    when(facilityReferenceDataService.findOne(facilityUuid)).thenReturn(facilityDto);
    when(programReferenceDataService.findOne(programUuid)).thenReturn(programDto);
    when(periodService.getPeriod(processingPeriodUuid)).thenReturn(processingPeriodDto);

    BasicRequisitionDto basicRequisitionDto = basicRequisitionDtoBuilder.build(requisition);

    assertNotNull(basicRequisitionDto);
    assertEquals(requisition.getId(), basicRequisitionDto.getId());
    assertEquals(requisition.getEmergency(), basicRequisitionDto.getEmergency());
    assertEquals(facilityDto, basicRequisitionDto.getFacility());
    assertEquals(programDto, basicRequisitionDto.getProgram());
    assertEquals(processingPeriodDto, basicRequisitionDto.getProcessingPeriod());
    assertEquals(requisition.getModifiedDate(), basicRequisitionDto.getModifiedDate());
  }

  @Test
  public void shouldBuildDtoFromRequisitionWhenReferenceDataInstancesDoNotExist() {
    when(facilityReferenceDataService.findOne(facilityUuid)).thenReturn(null);
    when(programReferenceDataService.findOne(programUuid)).thenReturn(null);
    when(periodService.getPeriod(processingPeriodUuid)).thenReturn(null);

    BasicRequisitionDto basicRequisitionDto = basicRequisitionDtoBuilder.build(requisition);

    assertNotNull(basicRequisitionDto);
    assertNull(basicRequisitionDto.getFacility());
    assertNull(basicRequisitionDto.getProgram());
    assertNull(basicRequisitionDto.getProcessingPeriod());
  }

  @Test
  public void shouldCallReferenceDataIfPassedValuesAreNull() {
    when(facilityReferenceDataService.findOne(facilityUuid)).thenReturn(facilityDto);
    when(programReferenceDataService.findOne(programUuid)).thenReturn(programDto);
    when(periodService.getPeriod(processingPeriodUuid)).thenReturn(processingPeriodDto);

    BasicRequisitionDto basicRequisitionDto = basicRequisitionDtoBuilder
        .build(requisition, null, null);

    assertNotNull(basicRequisitionDto);
    assertEquals(requisition.getId(), basicRequisitionDto.getId());
    assertEquals(requisition.getEmergency(), basicRequisitionDto.getEmergency());
    assertEquals(facilityDto, basicRequisitionDto.getFacility());
    assertEquals(programDto, basicRequisitionDto.getProgram());
    assertEquals(processingPeriodDto, basicRequisitionDto.getProcessingPeriod());
    assertEquals(requisition.getModifiedDate(), basicRequisitionDto.getModifiedDate());

    verify(facilityReferenceDataService).findOne(facilityUuid);
    verify(programReferenceDataService).findOne(programUuid);
    verify(periodService).getPeriod(processingPeriodUuid);
  }

  @Test
  public void shouldNotCallReferenceDataIfPassedValuesAreNotNull() {
    when(periodService.getPeriod(processingPeriodUuid)).thenReturn(processingPeriodDto);

    BasicRequisitionDto basicRequisitionDto = basicRequisitionDtoBuilder
        .build(requisition, facilityDto, programDto);

    assertNotNull(basicRequisitionDto);
    assertEquals(requisition.getId(), basicRequisitionDto.getId());
    assertEquals(requisition.getEmergency(), basicRequisitionDto.getEmergency());
    assertEquals(facilityDto, basicRequisitionDto.getFacility());
    assertEquals(programDto, basicRequisitionDto.getProgram());
    assertEquals(processingPeriodDto, basicRequisitionDto.getProcessingPeriod());
    assertEquals(requisition.getModifiedDate(), basicRequisitionDto.getModifiedDate());

    verify(facilityReferenceDataService, never()).findOne(any(UUID.class));
    verify(programReferenceDataService, never()).findOne(any(UUID.class));
    verify(periodService).getPeriod(processingPeriodUuid);
  }

  private Requisition buildRequisition() {
    Requisition requisition = new Requisition(facilityUuid, programUuid, processingPeriodUuid,
        RequisitionStatus.INITIATED, false);
    requisition.setId(requisitionUuid);
    requisition.setSupervisoryNodeId(supervisoryNodeUuid);
    requisition.setModifiedDate(ZonedDateTime.now());

    return requisition;
  }
}
