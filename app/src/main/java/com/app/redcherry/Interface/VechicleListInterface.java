package com.app.redcherry.Interface;

import com.app.redcherry.Model.VechicleDetails;


/**
 * Created by Rakshith on 10/29/2015.
 */
public interface VechicleListInterface {

    void OnVechicleSelected(VechicleDetails vechicleDetails);
    void OnVechicleDelete(VechicleDetails vechicleDetails);
    void OnVechicleEdit(VechicleDetails vechicleDetails);

}
