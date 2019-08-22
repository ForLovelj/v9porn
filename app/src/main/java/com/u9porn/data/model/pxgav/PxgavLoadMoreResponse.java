package com.u9porn.data.model.pxgav;

import java.util.List;

/**
 *
 * @author flymegoc
 * @date 2018/2/1
 */

public class PxgavLoadMoreResponse {

    private boolean success;
    private DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String items;
        private String hidePagNext;
        private String hidePagPrev;
        private ArgsBean args;

        public String getItems() {
            return items;
        }

        public void setItems(String items) {
            this.items = items;
        }

        public String getHidePagNext() {
            return hidePagNext;
        }

        public void setHidePagNext(String hidePagNext) {
            this.hidePagNext = hidePagNext;
        }

        public String getHidePagPrev() {
            return hidePagPrev;
        }

        public void setHidePagPrev(String hidePagPrev) {
            this.hidePagPrev = hidePagPrev;
        }

        public ArgsBean getArgs() {
            return args;
        }

        public void setArgs(ArgsBean args) {
            this.args = args;
        }

        public static class ArgsBean {
            /**
             * post_status : publish
             * post_type : ["post"]
             * posts_per_page : 8
             * orderby : rand
             * offset : 31
             * ignore_sticky_posts : 1
             * paged : 3
             */

            private String post_status;
            private String posts_per_page;
            private String orderby;
            private int offset;
            private int ignore_sticky_posts;
            private String paged;
            private List<String> post_type;

            public String getPost_status() {
                return post_status;
            }

            public void setPost_status(String post_status) {
                this.post_status = post_status;
            }

            public String getPosts_per_page() {
                return posts_per_page;
            }

            public void setPosts_per_page(String posts_per_page) {
                this.posts_per_page = posts_per_page;
            }

            public String getOrderby() {
                return orderby;
            }

            public void setOrderby(String orderby) {
                this.orderby = orderby;
            }

            public int getOffset() {
                return offset;
            }

            public void setOffset(int offset) {
                this.offset = offset;
            }

            public int getIgnore_sticky_posts() {
                return ignore_sticky_posts;
            }

            public void setIgnore_sticky_posts(int ignore_sticky_posts) {
                this.ignore_sticky_posts = ignore_sticky_posts;
            }

            public String getPaged() {
                return paged;
            }

            public void setPaged(String paged) {
                this.paged = paged;
            }

            public List<String> getPost_type() {
                return post_type;
            }

            public void setPost_type(List<String> post_type) {
                this.post_type = post_type;
            }
        }
    }
}
