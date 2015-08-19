package com.wlj.chuangbaba.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.wlj.bean.Base;
import com.wlj.chuangbaba.R;
import com.wlj.chuangbaba.bean.Group;
import com.wlj.chuangbaba.bean.Project;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * 铝材零售
 * @author wlj
 *
 */
public class ExpandableAdapter extends BaseExpandableListAdapter{

         private LinkedList<Base> groupArray;
         private LinkedList<List<Base>> childArray;
//         private Context context;
         private LayoutInflater mInflater;
         private Drawable mIcon1,group_img;
         
		public ExpandableAdapter(Context context,LinkedList<Base> courseGroupList,LinkedList<List<Base>> childArray2){
			mInflater = LayoutInflater.from(context);
//        	 mInflater = ((Activity) context).getLayoutInflater();
//             this.context = constext;
             this.groupArray = courseGroupList;
             this.childArray = childArray2;
//             mIcon1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.group_img);
             mIcon1 = context.getResources().getDrawable(R.drawable.ic_launcher);
             group_img = context.getResources().getDrawable(R.drawable.ic_launcher);
             mIcon1.setBounds(0, 0, mIcon1.getMinimumWidth(), mIcon1.getMinimumHeight());
             group_img.setBounds(0, 0, 40, 40);
         }
        
        public int getGroupCount() {
            return groupArray.size();
        }

        public int getChildrenCount(int groupPosition) {
            if(childArray.size() == 0 )
            	return 0;
            return childArray.get(groupPosition).size();
        }

        public Base getGroup(int groupPosition) {
            return groupArray.get(groupPosition);
        }

        public Object getChild(int groupPosition, int childPosition) {
            return childArray.get(groupPosition).get(childPosition);
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public boolean hasStableIds() {

            return true;

          }
        
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
            
            return getGroupViewStub(getGroup(groupPosition),convertView,groupPosition);
        }

        public View getGroupViewStub(Base s,View convertView,int groupPosition) {
			// Layout parameters for the ExpandableListView
//        	ViewGroup.LayoutParams lp = new AbsListView.LayoutParams(
//					ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
			
        	ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_explist_group_text,null);

				holder = new ViewHolder();
				TextView text = (TextView) convertView.findViewById(R.id.text);
				text.setCompoundDrawables(group_img, null, null, null);;//
				text.setPadding(2, 2, 2,2);
				holder.text =text;
						
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Group g = (Group)s;
			holder.text.setText(g.getName());
			holder.map =  g;
			holder.groupPosition = groupPosition;
        	
			return convertView;
		}
        
        public View getChildView(int groupPosition, int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {
					
        	return getView(groupPosition, childPosition, convertView, parent);
           

        }

        public View getView(int groupPosition, int childPosition,
				View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_list_icon_text,null);
				
				holder = new ViewHolder();
				TextView text = (TextView) convertView.findViewById(R.id.text);
//				TextView tixin = (TextView) convertView.findViewById(R.id.tixin);
				text.setPadding(12, 2, 2,2);
				text.setCompoundDrawables(mIcon1, null, null, null);;//this
				holder.text = text;
//				holder.tixin = tixin;
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Project project = (Project)childArray.get(groupPosition).get(childPosition);
			String name = project.getName();
			holder.text.setText(name);
//			holder.icon.setImageBitmap(mIcon1);
			holder.map =  project;
			holder.groupPosition = groupPosition;
			holder.childPosition = childPosition;
			
//			convertView.setBackgroundColor(Color.WHITE);
			return convertView;
		}
        
        
        public class ViewHolder {
    		TextView text;
//    		public TextView tixin;
    		public int groupPosition;
    		public int childPosition = -1;
    		public Base map;
    	}
    }