package com.wgz.ant.myappframework.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wgz.ant.myappframework.R;

import java.util.List;
import java.util.Map;

/**
 * Created by qwerr on 2015/11/25.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewholder> {
    private Context context;
    private List<Map<String , Object>> data;
    private OnItemClickLitener mOnItemClickLitener;

    public RecycleAdapter(List<Map<String, Object>> data,Context context) {
        this.context = context;
        this.data = data;
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);

    }
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewholder holder  = new MyViewholder(LayoutInflater.from(
                context).inflate(R.layout.recycler_item, parent,
                false));

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewholder holder, int position) {
        holder.textView1.setText(data.get(position).get("gname").toString());
        holder.pid.setText(data.get(position).get("pid").toString());
        holder.id.setText(data.get(position).get("id").toString());
        if (mOnItemClickLitener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public  class MyViewholder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView pid;
        TextView id;

        public MyViewholder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.mangroup_name);
            pid = (TextView) itemView.findViewById(R.id.gmpid);
            id = (TextView) itemView.findViewById(R.id.gmid);


        }



    }

}
