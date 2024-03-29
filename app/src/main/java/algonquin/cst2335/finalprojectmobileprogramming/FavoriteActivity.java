package algonquin.cst2335.finalprojectmobileprogramming;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalprojectmobileprogramming.data.LocationItem;
import algonquin.cst2335.finalprojectmobileprogramming.databinding.ActivityFavoriteBinding;
import algonquin.cst2335.finalprojectmobileprogramming.databinding.ActivityMainBinding;
import algonquin.cst2335.finalprojectmobileprogramming.databinding.LocationItemBinding;

public class FavoriteActivity extends AppCompatActivity {

    ActivityFavoriteBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private List<LocationItem> locationItems = new ArrayList<>(); // 假设的数据列表


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_favorite);
        // Initialize View Binding
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // create Toolbar
        setSupportActionBar(binding.toolbarLayout.toolbar);

        // The layout manager specifies RecyclerView items is arranged in a vertical column.
        binding.favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // The adapter is set up for RecyclerView, and is responsible for binding the data
        // from the data source to each entry of RecyclerView.
        binding.favoritesRecyclerView.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // 使用View Binding来为每个项（item）创建视图
                LocationItemBinding itemBinding = LocationItemBinding.inflate
                        (LayoutInflater.from(parent.getContext()), parent, false);
                return new MyViewHolder(itemBinding);
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                // 获取当前位置的LocationItem对象
                LocationItem item = locationItems.get(position); // 假设这是你的数据源中的项
                holder.bind(item);
                // 删除按钮的逻辑...
            }

            @Override
            public int getItemCount() {
                return locationItems.size(); // 假设这是你的数据源大小
            }
        });
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        // 使用View Binding类的实例
        private final LocationItemBinding binding;

        MyViewHolder(LocationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(LocationItem item) {
            // 使用binding来更新视图
            binding.name.setText(item.getName());
            binding.latitude.setText(String.valueOf(item.getLatitude()));
            binding.longitude.setText(String.valueOf(item.getLongitude()));
            // 绑定删除按钮的事件...
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.all_delete) {
            showConfirmDeleteDialog();
        } else if (itemId == R.id.location_instruction) {
                Toast.makeText(this, getString(R.string.FavoriteActivity_Instruction), Toast.LENGTH_SHORT).show();
        } else {
                throw new IllegalStateException(getString(R.string.error) + itemId);
        }
        return true;
    }

    private void showConfirmDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除所有收藏的位置吗？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 删除所有位置的逻辑
                        deleteAllLocations();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void deleteAllLocations() {
        // 这里调用删除所有位置的方法，例如使用ViewModel或直接操作数据库
    }
}