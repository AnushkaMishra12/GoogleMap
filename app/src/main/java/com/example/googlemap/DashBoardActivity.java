package com.example.googlemap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

    private CheckBox checkSelectAll;
    private List<Item> itemList;
    private ItemAdapter adapter;
    private boolean isSelectAllChanging = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        ListView listView = findViewById(R.id.listView);
        checkSelectAll = findViewById(R.id.checkSelectAll);
        ImageView location = findViewById(R.id.location);
        location.setOnClickListener(v -> {
            Intent i = new Intent(DashBoardActivity.this, MainActivity.class);
            startActivity(i);
        });

        itemList = new ArrayList<>();
        itemList.add(new Item("Option One", false));
        itemList.add(new Item("Option Two", false));
        itemList.add(new Item("Option Three", false));
        itemList.add(new Item("Option four", false));

        adapter = new ItemAdapter();
        listView.setAdapter(adapter);

        checkSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isSelectAllChanging) {
                isSelectAllChanging = true;
                adapter.selectAll(isChecked);
                isSelectAllChanging = false;
            }
        });
    }

    private static class Item {
        private final String name;
        private boolean isChecked;

        public Item(String name, boolean isChecked) {
            this.name = name;
            this.isChecked = isChecked;
        }

        public String getName() {
            return name;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }

    private class ItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Item getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_list, parent, false);
                holder = new ViewHolder();
                holder.checkBox = convertView.findViewById(R.id.checkBox);
                holder.textView = convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Item currentItem = getItem(position);

            holder.textView.setText(currentItem.getName());
            holder.checkBox.setChecked(currentItem.isChecked());

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!isSelectAllChanging) {
                    currentItem.setChecked(isChecked);
                    if (!isChecked) {
                        isSelectAllChanging = true;
                        checkSelectAll.setChecked(false);
                        isSelectAllChanging = false;
                    } else {
                        updateSelectAllCheckbox();
                    }
                }
            });

            return convertView;
        }

        private class ViewHolder {
            CheckBox checkBox;
            TextView textView;
        }

        public void selectAll(boolean isSelected) {
            for (Item item : itemList) {
                item.setChecked(isSelected);
            }
            notifyDataSetChanged();
        }
        private void updateSelectAllCheckbox() {
            boolean allChecked = true;
            for (Item item : itemList) {
                if (!item.isChecked()) {
                    allChecked = false;
                    break;
                }
            }
            if (allChecked) {
                isSelectAllChanging = true;
                checkSelectAll.setChecked(true);
                isSelectAllChanging = false;
            }
        }
    }
}
