package com.geminno.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.geminno.iccardmanagerapplication.Car;
import com.geminno.iccardmanagerapplication.R;
import com.geminno.request.HttpUtil;
import com.geminno.request.ProgressSubscriber;
import com.geminno.request.SubscriberOnNextListener;
import com.geminno.utils.Dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xu on 2017/7/7.
 */

public class BindFragment extends BaseFragment {
    private EditText editText_input, editText_phone;
    private Button bind;
    private EditText show_content;
    private SwipeMenuListView car_listView;
    public static List<Car> carList = new ArrayList<>();
    CarAdapter carAdapter;
    Dao dao;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.bind_fragment, null);
        editText_input = (EditText) view.findViewById(R.id.edit_input);
        bind = (Button) view.findViewById(R.id.btn_bind);
        editText_phone = (EditText) view.findViewById(R.id.edit_tel);
        car_listView = (SwipeMenuListView) view.findViewById(R.id.car_listView);
        dao = new Dao(context);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        context.getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(60));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        car_listView.setMenuCreator(creator);
        car_listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        delete(carList.get(0).getCph(), carList.get(0).getPhone());
                        break;

                }
                return false;
            }
        });
        return view;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void initData() {
        super.initData();
        carList = dao.getChe();
        carAdapter = new CarAdapter();
        car_listView.setAdapter(carAdapter);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bind(editText_input.getText().toString(), editText_phone.getText().toString());

            }
        });
    }

    private void delete(String cph, String phone) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                dao.delete(carList.get(0).getCph());
                carList.clear();
                carAdapter.notifyDataSetChanged();
            }
        };
        HttpUtil.getInstance().delete_data(new ProgressSubscriber<String>(onNextListener, getActivity()), cph, phone);
    }

    /**
     * 绑定车牌
     *
     * @param cph
     * @param phone
     */
    private void bind(final String cph, final String phone) {
        if ("".equals(cph)) {
            Toast.makeText(context, "车牌号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.length()!=11){
            Toast.makeText(context, "请输入正确的11位手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                carList.clear();
                if (dao.isExists(cph)) {
                    dao.delete(cph);
                    dao.saveInfos(cph, phone);
                    Car car = new Car();
                    car.setCph(cph);
                    car.setPhone(phone);
                    carList.add(car);
                } else {
                    dao.saveInfos(cph, phone);
                    Car car = new Car();
                    car.setCph(cph);
                    car.setPhone(phone);
                    carList.add(car);
                    carAdapter.notifyDataSetChanged();
                }
                editText_input.setText(null);
                editText_phone.setText(null);
            }

        };
        HttpUtil.getInstance().bind_data(new ProgressSubscriber<String>(onNextListener, getActivity()), cph, phone);
    }

    public class CarAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return carList.size();
        }

        @Override
        public Object getItem(int i) {
            return carList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(context, R.layout.car_bind, null);
                viewHolder.carName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.carName.setText(carList.get(i).getCph());
            return convertView;
        }
    }

    public class ViewHolder {
        TextView carName;
    }
}
