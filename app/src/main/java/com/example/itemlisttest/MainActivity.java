package com.example.itemlisttest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.itemlisttest.adapter.ItemVerticalAdapter;
import com.example.itemlisttest.dataClass.ItemContent;
import com.example.itemlisttest.dataClass.ShopItemInfo;
import com.example.itemlisttest.dataClass.ShopItemPackage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.item_new_recyclerView1)
    RecyclerView item_recyclerView1;
    @BindView(R.id.nested_item_new)
    NestedScrollView nested_item_new;

    private ItemVerticalAdapter newAdapter1;
    private ArrayList<ShopItemPackage> item_list1;

    /* 상품 리스트 묶음 번호 */
    private Integer package_num;
    /* 상품 리스트 묶음 이름의 리스트 */
    private ArrayList<String> package_name_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        nested_item_new = (NestedScrollView) findViewById(R.id.nested_item_new);

        item_list1 = new ArrayList<>();
        /* 상품 목록 리스트의 이름 리스트 생성*/
        package_name_list.add("급상승");
        package_name_list.add("신상품 best");
        /* 수직 리사이클러뷰의 하나의 아이템에 수평 리사이클러뷰의 아이템을 수평 방향으로 배치 설정, 어댑터 지정
         * (ex)  수평 리사이클러뷰
         *       수평 리사이클러뷰
         *       수평 리사이클러뷰
         * */
        /* 첫번째 리사이클러뷰*/
        newAdapter1 = new ItemVerticalAdapter(item_list1, getApplicationContext()); //New Adapter 안에 horizontal adapter를 선언하여 이에 대한 레이아웃을 horizontal로 지정
        item_recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        item_recyclerView1.setAdapter(newAdapter1);

        loadItems(nested_item_new, getApplicationContext());

    }

    /*첫번째 Fragment Item의 정보를 얻는 함수*/
    public void GetItemInfo1(Integer page_num, final String package_name) {
        String authorization = "zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        String accept = "application/json";

        ItemInfoService itemInfoService = ServiceGenerator.createService(ItemInfoService.class);
        retrofit2.Call<ShopItemInfo> request = itemInfoService.ItemInfo(page_num, 6, "id,asc", "heronation", "cafe24", authorization, accept);
        request.enqueue(new Callback<ShopItemInfo>() {
            @Override
            public void onResponse(Call<ShopItemInfo> call, Response<ShopItemInfo> response) {
                System.out.println("Response" + response.code());
                if (response.code() == 200) {
                    //아이템의 데이터를 받는 리스트
                    ArrayList<ItemContent> item_info = new ArrayList<>();
                    ShopItemInfo shopItemInfo = response.body();
                    /* Shop 목록을 생성함 */
                    for (int i = 0; i < shopItemInfo.getContent().size(); i++) {
                        item_info.add(shopItemInfo.getContent().get(i));
                    }
                    item_list1.add(new ShopItemPackage(package_name, item_info));
                    newAdapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ShopItemInfo> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    //package 넘버가 page 넘버 (임의로 이렇게 구현해둠 변경 필요)
    /**
     * 동적 로딩을 위한 NestedScrollView의 아래 부분을 인식
     **/
    public void loadItems(NestedScrollView nestedScrollView, final Context context) {
        package_num = 1;
        GetItemInfo1(package_num, package_name_list.get(package_num - 1));
        package_num += 1;
        GetItemInfo1(package_num, package_name_list.get(package_num - 1));
        item_recyclerView1.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!item_recyclerView1.canScrollVertically(1)){
                    if (package_num < 8) {
                        package_num += 1;
                        GetItemInfo1(package_num, "");
                        Log.d("qqqq",package_num.toString());
                    }
                }
            }
        });
    }


    //인터페이스 - 추상 메소드(구현부가 없는 메시드)의 모임
    /* retrofit은 인터페이스에 기술된 명세를 Http API(호출 가능한 객체)로 전환해줌
    => 우리가 요청할 API들에 대한 명세만을 Interface에 기술해두면 됨.
     */
    /* 사용자 정보를 서버에서 받아오는 인터페이스*/
    public interface ItemInfoService {
        @GET("api/items/test")
        retrofit2.Call<ShopItemInfo> ItemInfo(@Query("page") Integer page,
                                              @Query("size") Integer size,
                                              @Query("sort") String sort,
                                              @Query("storeId") String storeId,
                                              @Query("storeType") String storeType,
                                              @Header("authorization") String authorization,
                                              @Header("Accept") String accept);
    }
}