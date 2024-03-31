/**
 * Student Name: Ting Cheng
 * Professor: Samira Ouaaz
 * Due Date: Apr. 1,2024
 * Description:  CST2355-031 FinalAssignment
 * Section: 031
 * Modify Date: Mar. 31,2024
 */
package algonquin.cst2335.finalprojectmobileprogramming.data;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

//Class stores all of the variables representing data that is shown on this page?
//I seem NOT need this?

public class LocationViewModel {
    public MutableLiveData<ArrayList<LocationItem>> messages = new MutableLiveData<>();
}
