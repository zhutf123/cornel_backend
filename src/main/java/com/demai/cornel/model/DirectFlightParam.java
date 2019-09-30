package com.demai.cornel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DirectFlightParam {
    private String depCity;
    private String arrCity;
    private int status; // 直飞数据是否有效 0无效，1有效

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + depCity.hashCode();
        result = result * 31 + arrCity.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DirectFlightParam)) {
            return false;
        }
        DirectFlightParam param = (DirectFlightParam) obj;
        return param.arrCity.equals(arrCity) && param.depCity.equals(depCity);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
