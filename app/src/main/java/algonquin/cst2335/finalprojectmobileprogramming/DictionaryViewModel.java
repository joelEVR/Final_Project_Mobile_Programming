package algonquin.cst2335.finalprojectmobileprogramming;
import android.widget.Button;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * ViewModel class for DictionaryActivity
 * To avoid Android from deleting the current screen and creating a new one for the new rotated orientation.
 */
public class DictionaryViewModel extends ViewModel {
    public MutableLiveData<String> searchText = new MutableLiveData<>();

    public MutableLiveData<ArrayList<String>> definitions = new MutableLiveData< >();

}
