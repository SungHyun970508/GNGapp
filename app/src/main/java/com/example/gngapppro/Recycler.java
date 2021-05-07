package com.example.gngapppro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {
    private ArrayList<Shopping_class> mData = null;


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView proName ;
        TextView proCost ;
        TextView proCnt ;
        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            proName = itemView.findViewById(R.id.product_Name) ;
            proCost = itemView.findViewById(R.id.product_Cost) ;
            proCnt = itemView.findViewById(R.id.product_Cnt) ;
        }
    }

    Recycler(ArrayList<Shopping_class> list){
        mData = list;
    }

    @NonNull
    @Override
    public Recycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.list_item, parent, false) ;
        Recycler.ViewHolder vh = new Recycler.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler.ViewHolder holder, int position) {
        Shopping_class item = mData.get(position) ;
        holder.proName.setText(item.getproName()) ;
        holder.proCost.setText(item.getproCost()) ;
        holder.proCnt.setText(item.getcount()) ;


    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}