package com.freddiemac.housing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Scope("prototype")
@Component
@AllArgsConstructor
@Data
public class InternalReturnRateData extends TargetSuggestionData {

    @Override
    public void setData()
    {
        //Todo: calculate internal rate of return from data returned
    }

}
