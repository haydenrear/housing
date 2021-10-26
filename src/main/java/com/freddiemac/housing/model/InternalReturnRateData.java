package com.freddiemac.housing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@EqualsAndHashCode(callSuper = true)
@Scope("prototype")
@Component
@AllArgsConstructor
@Data
@NoArgsConstructor
public class InternalReturnRateData extends TargetSuggestionData {

    double currentPrice;
    double projectedPrice;

    public InternalReturnRateData(double currentPrice)
    {
        this.currentPrice = currentPrice;
    }

    @Override
    @PostConstruct
    public void setData()
    {
        this.data = new Double[] {this.currentPrice};
        //Todo: calculate return
    }


    public void setProjectedPrice(double price)
    {
        this.projectedPrice = price;
    }
}
