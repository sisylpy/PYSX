package com.swolo.lpy.pysx.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swolo.lpy.pysx.R;
import com.swolo.lpy.pysx.main.modal.ComuFatherGoods;

import java.util.List;

public class FatherGoodsViewAdapter extends RecyclerView.Adapter<FatherGoodsViewAdapter.InnerHolder> {

    private List<ComuFatherGoods> mComuFatherGoods;
    private OnFatherGoodsClickListener mListener;


    public void setEmpty() {
        if(mComuFatherGoods.size() > 0){
            System.out.println("is apididiididiidpttterrr!");
            mComuFatherGoods.removeAll(mComuFatherGoods);
        }


        this.notifyDataSetChanged();
    }
    public void setData(List<ComuFatherGoods> suppliers) {
        this.mComuFatherGoods = suppliers;
        this.notifyDataSetChanged();
    }

    public void setOnClickFatherGoodsListener(OnFatherGoodsClickListener listener) {
        this.mListener = listener;
    }

    public interface OnFatherGoodsClickListener {
        void onItemClick(ComuFatherGoods comuFatherGoods);
    }

    @Override
    public InnerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_father_view, null);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(InnerHolder holder, int position) {
        holder.setData(mComuFatherGoods.get(position));
    }

    @Override
    public int getItemCount() {
        if (mComuFatherGoods != null) {
            return mComuFatherGoods.size();
        }
        return 0;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        private final TextView tv;
        private ComuFatherGoods curComuFatherGoods;

        public InnerHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.item_father);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onItemClick(curComuFatherGoods);
                    }
                }
            });

        }

        public void setData(ComuFatherGoods comuFatherGoods) {
            this.curComuFatherGoods = comuFatherGoods;
            tv.setText(comuFatherGoods.nxCfgFatherGoodsName);

        }
    }

}
