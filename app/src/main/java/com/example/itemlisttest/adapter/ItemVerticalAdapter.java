package com.example.itemlisttest.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itemlisttest.R;
import com.example.itemlisttest.dataClass.ShopItemPackage;

import java.util.ArrayList;

public class ItemVerticalAdapter extends RecyclerView.Adapter<ItemVerticalAdapter.VerticalViewHolder> {
    private ArrayList<ShopItemPackage> itemList_List;
    private Context context;

    public ItemVerticalAdapter(ArrayList<ShopItemPackage> itemList_List, Context context) {
        this.itemList_List = itemList_List;
        this.context = context;
    }

    /* viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성*/
    @NonNull
    @Override
    public ItemVerticalAdapter.VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* 아이템 하나를 나타내는 xml파일을 뷰에 바인딩 */
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_vertical_list_item,parent,false);
        /* 뷰홀더 객체 생성 */
        VerticalViewHolder holder=new VerticalViewHolder(view);
        return holder;
    }

    /* position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시 */
    @Override
    public void onBindViewHolder(@NonNull ItemVerticalAdapter.VerticalViewHolder holder, int position) {
        //수평 리사이클러뷰 어댑터
        if(position<2) {
            ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(itemList_List.get(position).getShopItems(), context);
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setAdapter(adapter);
            holder.packageName.setText(itemList_List.get(position).getPackageName());
        }else{
            ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(itemList_List.get(position).getShopItems(), context);
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
            holder.recyclerView.setAdapter(adapter);
            holder.packageName.setText(itemList_List.get(position).getPackageName());
        }

    }

    @Override
    public int getItemCount() {
        return itemList_List.size();
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder{
        protected RecyclerView recyclerView;
        protected TextView packageName;
        public VerticalViewHolder(View view)
        {
            super(view);
            this.recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewVertical);
            this.packageName=(TextView)view.findViewById(R.id.packed_item_name);
        }
    }
}
