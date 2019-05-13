package com.example.learningplatform.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GoodsEntity {

    /**
     * status : success
     * data : [{"id":1,"name":"数据库","price":10,"createAt":null,"updateAt":null,"faceUrl":null,"description":"这本书是数据库方面的书籍。","ownerId":1},{"id":2,"name":"物联网专业英语","price":20,"createAt":null,"updateAt":null,"faceUrl":null,"description":"物联网专业书籍","ownerId":2},{"id":3,"name":"高等数学II","price":10,"createAt":null,"updateAt":null,"faceUrl":null,"description":"工科学院基础课","ownerId":2},{"id":4,"name":"物联网技术概论","price":100,"createAt":null,"updateAt":null,"faceUrl":null,"description":"物联网工程专业书籍，主要介绍了物联网的几大技术和发展前景。","ownerId":3}]
     */

    private String status;
    private List<GoodsInfo> data;
    public GoodsEntity(){}
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GoodsInfo> getData() {
        return data;
    }

    public void setData(List<GoodsInfo> data) {
        this.data = data;
    }

    public static class GoodsInfo implements Parcelable {
        /**
         * id : 1
         * name : 数据库
         * price : 10
         * createAt : null
         * updateAt : null
         * faceUrl : null
         * description : 这本书是数据库方面的书籍。
         * ownerId : 1
         */

        private int id = -1;
        private String name = "";
        private int price;
        private String createAt;
        private String updateAt;
        private String faceUrl;
        private String description;
        private int ownerId;
        public GoodsInfo(){}
        protected GoodsInfo(Parcel in) {
            id = in.readInt();
            name = in.readString();
            price = in.readInt();
            description = in.readString();
            ownerId = in.readInt();
        }

        public static final Creator<GoodsInfo> CREATOR = new Creator<GoodsInfo>() {
            @Override
            public GoodsInfo createFromParcel(Parcel in) {
                return new GoodsInfo(in);
            }

            @Override
            public GoodsInfo[] newArray(int size) {
                return new GoodsInfo[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public Object getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public String getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(String updateAt) {
            this.updateAt = updateAt;
        }

        public String getFaceUrl() {
            return faceUrl;
        }

        public void setFaceUrl(String faceUrl) {
            this.faceUrl = faceUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(int ownerId) {
            this.ownerId = ownerId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeInt(price);
            dest.writeString(description);
            dest.writeInt(ownerId);
        }
    }
}
