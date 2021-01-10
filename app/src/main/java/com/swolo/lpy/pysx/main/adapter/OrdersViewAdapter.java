package com.swolo.lpy.pysx.main.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swolo.lpy.pysx.R;
import com.swolo.lpy.pysx.main.modal.ComuGoods;
import com.swolo.lpy.pysx.main.modal.Orders;

import java.util.List;

public class OrdersViewAdapter extends RecyclerView.Adapter<OrdersViewAdapter.InnerHolder> {

    private List<Orders> ordersList;
    public int focusPosition = 0;

    public void deleteItem(Orders detail) {
        int index = ordersList.indexOf(detail);
        if (index > -1) {
            ordersList.remove(index);

            if(ordersList.size() > 0){
                this.notifyItemRemoved(index);

            }
        }
    }



    private onClickOrderListener mListener;


    public void setOnClickOrderListner(onClickOrderListener listner) {
        this.mListener = listner;
    }

    public interface onClickOrderListener {
        void onItemClick(Orders orders, Integer position);
    }


    public void setData(List<Orders> orders) {
        this.ordersList = orders;
        this.notifyDataSetChanged();
    }


    @Override
    public InnerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_order_view, null);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(final InnerHolder holder, final int position) {
        if (holder != null) {
            holder.setData(ordersList.get(position));
            if (ordersList.get(position).hasChoice) {
                holder.setFocus();
            } else {
                holder.setUnFocus();
            }
            if(!ordersList.get(position).getNxRoWeight().equals("-1")){
                holder.setWeight(ordersList.get(position));
            }else {

            }
        }

        if (mListener != null) {
            //點擊事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(ordersList.get(position), position);
                }
            });
        }
    }

    public void changeItemStatus(int targetPos) {
        focusPosition = targetPos;
        for (Orders order : ordersList) {
            order.setHasChoice(false);
        }
        ordersList.get(targetPos).setHasChoice(true);
        notifyDataSetChanged();
    }

    public void updateOrders(Orders orders){
        ordersList.get(focusPosition).setNxRoWeight(orders.nxRoWeight);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (ordersList != null) {
            return ordersList.size();
        }
        return 0;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        private final TextView tv;
        private View weighBack;
        private View weighCursor;
        private TextView weighText;
        private TextView itemRemark;

        public InnerHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.item_order);
            itemRemark = itemView.findViewById(R.id.item_remark);
            weighBack = itemView.findViewById(R.id.weigh_back);
            weighCursor = itemView.findViewById(R.id.weigh_cousor);
            weighText = itemView.findViewById(R.id.weigh_text);

        }

        public void setData(Orders orders) {
            String quanityt = orders.nxRoQuantity;
            String standard = orders.nxRoStandard;
            tv.setText(quanityt + standard);
//            weighText.setId(orders.nxRestrauntOrdersId);
            String remark = orders.nxRoRemark;
            itemRemark.setText(remark);


        }

        private void setFocus() {
            weighText.setText("");
            tv.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
            weighBack.setBackgroundColor(itemView.getResources().getColor(R.color.mintcream));
            weighCursor.setVisibility(View.VISIBLE);
        }

        private void setUnFocus() {
            weighText.setText("");
            tv.setTextColor(itemView.getResources().getColor(R.color.black));
            weighBack.setBackgroundColor(itemView.getResources().getColor(R.color.white));
            weighCursor.setVisibility(View.INVISIBLE);
        }


        private void setWeight(Orders orders){
            weighText.setText(orders.nxRoWeight);
        }

        private void setEmptyWeight(Orders orders){
            weighText.setText("");
        }


    }
}
