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

package org.openlmis.requisition.dto;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public class UserDto {
  private UUID id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private boolean verified;
  private FacilityDto homeFacility;
  private Set<RoleAssignmentDto> roleAssignments;
  private Boolean allowNotify;
  private boolean active;

  /**
   * Prints the name of the user for display purposes.
   * The format is "firstName lastName". If one of them is missing, it is
   * omitted and the space is trimmed. If they are both missing, the
   * user name is used.
   * @return the name of the user for display purposes
   */
  public String printName() {
    if (StringUtils.isBlank(firstName) && StringUtils.isBlank(lastName)) {
      return username;
    } else {
      return StringUtils.trim(StringUtils.defaultString(firstName) + ' '
          + StringUtils.defaultString(lastName));
    }
  }

  public boolean allowNotify() {
    return this.getAllowNotify() != null && this.getAllowNotify();
  }

  public boolean activeAndVerified() {
    return this.isActive() && this.isVerified();
  }
}
