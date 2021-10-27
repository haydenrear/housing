package com.freddiemac.housing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "targetSuggestionData")
@Scope("prototype")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class TargetSuggestionData extends SuggestionData {

    //Todo:
    String address;

    @Override
    public void setData()
    {

    }

}
