package wyqj.cancerprevent.doctorversion.activity.square;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.lib.bigimage.ViewBigImageActivity;
import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.utils.DensityUtil;
import com.kaws.lib.common.utils.ScreenSizeUtil;
import com.kaws.lib.common.widget.MyGridView;
import com.kaws.lib.common.widget.MyListView;
import com.kaws.lib.fresco.Image;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.MedicalBookEntity;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class NewIllNoteActivity extends LoadBaseActivity {

    private TextView txtHeadBasicInfoNewIllNote;
    private TextView txtTimeBasicInfoNewIllNote;
    private TextView txtSexBasicInfoNewIllNote;
    private TextView txtPatientBasicInfoNewIllNote;
    private TextView edtNameIllNote;
    private EditText edtAgeIllNote;
    private TextView tvIllnoteOtherinfoTitle;
    private RelativeLayout rlytOthersNewIllNote;
    /**症状描述头*/
    private RelativeLayout rlytTitleSyndromeNewIllNote;
    /** 症状描述内容*/
    private TextView txtContentSyndromeNewIllNote;
    /** 治疗过程头*/
    private RelativeLayout rlytTitleTreatmentNewIllNote;
    /** 治疗过程内容*/
    private TextView txtContentTreatmentNewIllNote;
    /** 既往病史头*/
    private RelativeLayout rlytTitleHistoryNewIllNote;
    /** 既往病史内容 */
    private TextView txtContentHistoryNewIllNote;
    /** 其他资料内容*/
    private MyListView listOthersNewIllNote;

    private String patientUserkey;
    /** otherAdapter 数据源*/
    private List<MedicalBookEntity.OtherInfoEntity> othersDataList = new ArrayList<>();
    private String[] otherList = {"化验报告", "影像学检查", "病理检查", "确诊单", "用药记录", "复查记录", "住院及其它", "伤口拍照"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ill_note);
        setTitle("病历本");
        initView();
        setUpView();
    }

    private void initView() {

        txtHeadBasicInfoNewIllNote = (TextView) findViewById(R.id.txt_head_basic_info_new_ill_note);
        txtTimeBasicInfoNewIllNote = (TextView) findViewById(R.id.txt_time_basic_info_new_ill_note);
        txtSexBasicInfoNewIllNote = (TextView) findViewById(R.id.txt_sex_basic_info_new_ill_note);
        txtPatientBasicInfoNewIllNote = (TextView) findViewById(R.id.txt_patient_basic_info_new_ill_note);
        edtNameIllNote = (TextView) findViewById(R.id.edt_name_ill_note);
        edtAgeIllNote = (EditText) findViewById(R.id.edt_age_ill_note);
        tvIllnoteOtherinfoTitle = (TextView) findViewById(R.id.tv_illnote_otherinfo_title);
        rlytTitleSyndromeNewIllNote = (RelativeLayout) findViewById(R.id.rlyt_title_syndrome_new_ill_note);
        txtContentSyndromeNewIllNote = (TextView) findViewById(R.id.txt_content_syndrome_new_ill_note);
        rlytTitleTreatmentNewIllNote = (RelativeLayout) findViewById(R.id.rlyt_title_treatment_new_ill_note);
        txtContentTreatmentNewIllNote = (TextView) findViewById(R.id.txt_content_treatment_new_ill_note);
        txtContentHistoryNewIllNote = (TextView) findViewById(R.id.txt_content_history_new_ill_note);
        listOthersNewIllNote = (MyListView) findViewById(R.id.list_others_new_ill_note);
    }

    private void setUpView() {
        if (getIntent() != null) {
            patientUserkey = getIntent().getStringExtra(Constants.PATIENT_USERKEY);
        }
        setListViewHeightBasedOnChildren(listOthersNewIllNote);
        switchUiToShow();
        getIllNote();
    }

    /**
     * 获取病历本数据
     */
    private void getIllNote() {
        stopProgressDialog();
        final HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.IllNote(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, patientUserkey, new CustomCallBack<MedicalBookEntity>() {

            @Override
            public void onSuccess(MedicalBookEntity medicalBookEntity) {
                showContentView();
                stopProgressDialog();
                processData(medicalBookEntity);
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }

    private void processData(MedicalBookEntity medicalBookEntity) {
        edtNameIllNote.setText(medicalBookEntity.name);
        edtAgeIllNote.setText(String.valueOf(medicalBookEntity.age));
        txtSexBasicInfoNewIllNote.setText(medicalBookEntity.sex);
        if (medicalBookEntity.confirmedDate != null) {
            txtTimeBasicInfoNewIllNote.setText(medicalBookEntity.confirmedDate.substring(0, 10));
        }
        txtPatientBasicInfoNewIllNote.setText(appendDiseaseState(medicalBookEntity.diseases));
        txtContentSyndromeNewIllNote.setText(medicalBookEntity.symptom);
        txtContentTreatmentNewIllNote.setText(medicalBookEntity.treatment);
        txtContentHistoryNewIllNote.setText(medicalBookEntity.anamnesis);
        addList(medicalBookEntity);
        if (othersDataList.size() != 0) {
            OtherAdapter otherAdapter = new OtherAdapter(othersDataList);
            listOthersNewIllNote.setAdapter(otherAdapter);
        } else {
            tvIllnoteOtherinfoTitle.setVisibility(View.GONE);
        }

    }

    /**
     * 为null也add进去，否则会出现少条目，信息不对称的情况（处理null的情况）
     */
    private void addList(MedicalBookEntity medicalBookEntity) {
        othersDataList.clear();
        othersDataList.add(medicalBookEntity.assayReport);
        othersDataList.add(medicalBookEntity.imaging);
        othersDataList.add(medicalBookEntity.PathologicalReport);
        othersDataList.add(medicalBookEntity.diagnose);
        othersDataList.add(medicalBookEntity.medicineRecord);
        othersDataList.add(medicalBookEntity.reExamination);
        othersDataList.add(medicalBookEntity.inHospitalRecord);
        othersDataList.add(medicalBookEntity.woundPhoto);
    }

    /**
     * 页面不能修改
     */
    private void switchUiToShow() {
        edtNameIllNote.setEnabled(false);
        edtAgeIllNote.setEnabled(false);
        txtTimeBasicInfoNewIllNote.setClickable(false);
        rlytTitleSyndromeNewIllNote.setClickable(false);
        rlytTitleTreatmentNewIllNote.setClickable(false);
        txtHeadBasicInfoNewIllNote.setVisibility(View.GONE);
        txtSexBasicInfoNewIllNote.setVisibility(View.VISIBLE);
        txtContentSyndromeNewIllNote.setVisibility(View.VISIBLE);
        txtContentTreatmentNewIllNote.setVisibility(View.VISIBLE);
        txtContentHistoryNewIllNote.setVisibility(View.VISIBLE);
    }

    /** 拿返回的病历本里患病信息拼串*/
    private String appendDiseaseState(List<MedicalBookEntity.DiseasedStates> list) {
        String backStr = "";
        if (list.size() == 1) {
            return list.get(0).name;
        } else {
            StringBuilder stringBuilder = new StringBuilder(backStr);
            for (int i = 0; i < list.size(); i++) {
                String temp = list.get(i).name;
                stringBuilder.append(temp);
                if (i != list.size() - 1) {
                    stringBuilder.append(" - ");
                }
            }
            return backStr;
        }
    }

    /**
     * * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView  ***
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /** 其他资料的Adapter*/
    class OtherAdapter extends BaseAdapter {
        List<MedicalBookEntity.OtherInfoEntity> othersDataList;

        public OtherAdapter(List<MedicalBookEntity.OtherInfoEntity> othersDataList) {
            this.othersDataList = othersDataList;
        }

        @Override
        public int getCount() {
            return othersDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View childView = null;
            OtherViewHolder holder;
            if (childView == null) {
                childView = LayoutInflater.from(NewIllNoteActivity.this).inflate(R.layout.item_other_list_new_ill_note, viewGroup, false);
                holder = new OtherViewHolder(childView);
                childView.setTag(holder);
            } else {
                childView = view;
                holder = (OtherViewHolder) childView.getTag();
            }

            /**
             * 点击gridview空白处响应listview的点击事件
             */
            holder.gridView.setOnTouchInvalidPositionListener(new MyGridView.OnTouchInvalidPositionListener() {
                @Override
                public boolean onTouchInvalidPosition(int motionEvent) {
                    return false;
                }
            });
            /**
             * 点击查看大图
             */
            ArrayList<MedicalBookEntity.Images> imageuri = null;
            if (othersDataList!=null && othersDataList.get(i)!=null) {
                imageuri = (ArrayList<MedicalBookEntity.Images>) othersDataList.get(i).images;
            }
            final ArrayList<String> urlList = new ArrayList<String>();
            if (imageuri != null) {
                for (int j = 0; j < imageuri.size(); j++) {
                    urlList.add(imageuri.get(j).url);
                }
            }

            holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("code", position);//点击显示当前哪张图片
                    bundle.putInt("selet", 2);//只有一张图片是否显示1/1
                    bundle.putStringArrayList("imageuri", urlList);//图片的url集合
                    Intent intent = new Intent(NewIllNoteActivity.this, ViewBigImageActivity.class);
                    intent.putExtras(bundle);
                    NewIllNoteActivity.this.startActivity(intent);
                }
            });

            holder.txt_title_item_other_list_new_ill_note.setText(otherList[i]);
            if (othersDataList.get(i) != null) {
                if (othersDataList.get(i).content == null && othersDataList.get(i).images ==null) {
                    holder.txt_content_item_other_list_new_ill_note.setText("无");
                    holder.txt_more_item_other_list_new_ill_note.setVisibility(View.GONE);
                } else {
                    holder.txt_content_item_other_list_new_ill_note.setText(othersDataList.get(i).content);
                    /** 有内容则可点击进入详情*/
                    holder.ll_item_other_list_new_ill_note.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(NewIllNoteActivity.this, IllNoteOthersTimelineActivity.class);
                            intent.putExtra("position", i);
                            intent.putExtra("patientUserkey", patientUserkey);
                            startActivity(intent);
                        }
                    });
                }
                holder.gridView.setAdapter(new OtherGridAdapter(othersDataList.get(i).images,viewGroup.getContext()));
            } else {
                holder.txt_content_item_other_list_new_ill_note.setText("无");
                holder.txt_more_item_other_list_new_ill_note.setVisibility(View.GONE);
            }
            return childView;
        }

        /** OtherAdapter的ViewHolder*/
        class OtherViewHolder {
            public LinearLayout ll_item_other_list_new_ill_note;
            public TextView txt_title_item_other_list_new_ill_note;
            public TextView txt_more_item_other_list_new_ill_note;
            public TextView txt_content_item_other_list_new_ill_note;
            public MyGridView gridView;

            public OtherViewHolder(View childView) {
                ll_item_other_list_new_ill_note = (LinearLayout) childView.findViewById(R.id.ll_item_other_list_new_ill_note);
                txt_title_item_other_list_new_ill_note = (TextView) childView.findViewById(R.id.txt_title_item_other_list_new_ill_note);
                txt_more_item_other_list_new_ill_note = (TextView) childView.findViewById(R.id.txt_more_item_other_list_new_ill_note);
                txt_content_item_other_list_new_ill_note = (TextView) childView.findViewById(R.id.txt_content_item_other_list_new_ill_note);
                gridView = (MyGridView) childView.findViewById(R.id.gdv_item_other_list_new_ill_note);
            }
        }

        /** 图片的Adapter*/
        class OtherGridAdapter extends BaseAdapter {
            private final int itemWidthAndHeight;
            List<MedicalBookEntity.Images> list;

            public OtherGridAdapter(List<MedicalBookEntity.Images> list, Context context) {
                this.list = list;
                itemWidthAndHeight = (ScreenSizeUtil.getScreenWidth(context) - DensityUtil.dip2px(context, 32)) / 3;
            }

            @Override
            public int getCount() {
                if (list == null) {
                    return 0;
                } else if (list.size() > 3) {
                    return 3;
                } else {
                    return list.size();
                }
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }


            @Override
            public View getView(final int i, View view, ViewGroup viewGroup) {
                View childView = null;
                ViewHolder holder;
                if (childView == null) {
                    childView = LayoutInflater.from(NewIllNoteActivity.this).inflate(R.layout.item_nine_pics, viewGroup, false);
                    holder = new ViewHolder();
                    holder.itemPic = (SimpleDraweeView) childView.findViewById(R.id.img_item_nine_pics);
                    childView.setTag(holder);
                } else {
                    childView = view;
                    holder = (ViewHolder) childView.getTag();
                }
                holder.itemPic.setLayoutParams(new RelativeLayout.LayoutParams(itemWidthAndHeight, itemWidthAndHeight));
                Image.displayImage(holder.itemPic, Uri.parse(list.get(i).url), R.mipmap.default_pic);
                return childView;
            }

            class ViewHolder {
                SimpleDraweeView itemPic;
            }
        }
    }

}
