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

import algonquin.cst2335.finalprojectmobileprogramming.data.LocationDatabase;
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
                return  new MyViewHolder(itemBinding, position -> {
                    // 这里实现删除逻辑
                    LocationItem itemToRemove = locationItems.get(position);
                    locationItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, locationItems.size());
                    // 这里添加与数据库同步删除的代码
                });
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                // 获取当前位置的LocationItem对象
                LocationItem item = locationItems.get(position); // 假设这是你的数据源中的项
                holder.bind(item, position);
                // 删除按钮的逻辑...
            }

            @Override
            public int getItemCount() {
                return locationItems.size(); // 假设这是你的数据源大小
            }
        });

        loadLocationsFromDatabase();

    }


    public interface OnLocationItemDeleted {
        void onItemDeleted(int position);
    }


    private void loadLocationsFromDatabase() {
        new Thread(() -> {
            // To avoid creating multiple database instances, use the singleton mode
            LocationDatabase db = LocationDatabase.getDatabase(getApplicationContext());
            List<LocationItem> items = db.locationItemDao().getAllLocations(); // 假设你有这样的方法
            runOnUiThread(() -> {
                locationItems.clear();
                locationItems.addAll(items);
                binding.favoritesRecyclerView.getAdapter().notifyDataSetChanged();
            });
        }).start();
    }
    // error by using private static
//    private class MyViewHolder extends RecyclerView.ViewHolder {
//        // 使用View Binding类的实例
//        private final LocationItemBinding binding;
//
//        MyViewHolder(LocationItemBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//
//        void bind(LocationItem item) {
//            // 使用binding来更新视图
//            binding.name.setText(item.getName());
//            binding.latitude.setText(String.valueOf(item.getLatitude()));
//            binding.longitude.setText(String.valueOf(item.getLongitude()));
//            // 绑定删除按钮的事件...
//            binding.deleteButton.setOnClickListener(v -> {
//                // 在这里实现删除当前位置的逻辑
//                new Thread(() -> {
//                    LocationDatabase db = LocationDatabase.getDatabase(binding.getRoot().getContext());
//                    db.locationItemDao().deleteLocation(item); // 假设你有这样的方法
//                    // 从列表中移除项目，并在UI线程上通知适配器更改
//                    ((AppCompatActivity)binding.getRoot().getContext()).runOnUiThread(() -> {
//                        locationItems.remove(item);
//                        notifyDataSetChanged();
//                    });
//                }).start();
//            });
//        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private final LocationItemBinding binding;
            private final OnLocationItemDeleted deleteCallback;


            public MyViewHolder(LocationItemBinding binding, OnLocationItemDeleted deleteCallback) {
                super(binding.getRoot());
                this.binding = binding;
                this.deleteCallback = deleteCallback;
            }

            void bind(LocationItem item, int position) {
                binding.name.setText(item.getName());
                binding.latitude.setText(String.valueOf(item.getLatitude()));
                binding.longitude.setText(String.valueOf(item.getLongitude()));
                // 设置删除按钮的点击监听器
                binding.deleteButton.setOnClickListener(v -> {
                        // 调用删除回调
                    deleteCallback.onItemDeleted(position);
                });
            }
        }


    // Defines an interface to handle the deletion of location items
    public interface LocationItemListener {
        void onDeleteLocation(LocationItem item);
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
        new Thread(() -> {
            LocationDatabase db = LocationDatabase.getDatabase(getApplicationContext());
            db.locationItemDao().deleteAllLocations(); // 假设你有这样的方法
            // 从列表中移除所有项目，并在UI线程上通知适配器更改
            runOnUiThread(() -> {
                locationItems.clear();
                binding.favoritesRecyclerView.getAdapter().notifyDataSetChanged();
            });
        }).start();
    }
}