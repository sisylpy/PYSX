package com.swolo.lpy.pysx.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swolo.lpy.pysx.R;
import com.swolo.lpy.pysx.main.modal.ComuFatherGoods;
import com.swolo.lpy.pysx.main.modal.ComuGoods;

import java.util.List;

public class GoodsViewAdapter extends RecyclerView.Adapter<GoodsViewAdapter.InnerHolder> {

    private List<ComuGoods> comuGoodsList;
    private GoodsViewAdapter.OnGoodsClickListener mListener;

    public void setData(List<ComuGoods> comuGoods) {
        this.comuGoodsList = comuGoods;
        this.notifyDataSetChanged();
    }

    public void setOnClickGoodsListener(GoodsViewAdapter.OnGoodsClickListener listener) {
        this.mListener = listener;
    }

    public interface OnGoodsClickListener {
        void onItemClick(ComuGoods comuGoods);
    }

    @Override
    public InnerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = View.inflate(parent.getContext(), R.layout.item_goods_view, null);
        return  new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(InnerHolder holder, int position) {
        holder.setData(comuGoodsList.get(position));

    }

    @Override
    public int getItemCount() {
        if(comuGoodsList != null){
            return comuGoodsList.size();
        }
        return 0;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        private final TextView tv;
        private ComuGoods comuGoods;

        public InnerHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.item_goods);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onItemClick(comuGoods);
                    }
                }
            });
        }

        public void setData(ComuGoods comuGoods) {
            this.comuGoods = comuGoods;
            tv.setText(comuGoods.nxCgGoodsName);

        }
    }
}
