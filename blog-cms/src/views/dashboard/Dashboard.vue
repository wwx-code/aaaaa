<template>
    <div>
        <el-row class="panel-group">
            <el-col :span="8">
                <el-card class="card-panel">
                    <div class="card-panel-icon-wrapper">
                        <i class="el-icon-s-custom" style="font-size: 55px"></i>
                    </div>
                    <div class="card-panel-description">
                        <div class="card-panel-text">今日pv</div>
                        <span class="card-panel-num">{{ pv }}</span>
                    </div>
                </el-card>
            </el-col>

            <el-col :span="8">
                <el-card class="card-panel">
                    <div class="card-panel-icon-wrapper">
                        <i class="el-icon-document" style="font-size: 55px"></i>
                    </div>
                    <div class="card-panel-description">
                        <div class="card-panel-text">文章数</div>
                        <span class="card-panel-num">{{ blogCount }}</span>
                    </div>
                </el-card>
            </el-col>

            <el-col :span="8">
                <el-card class="card-panel">
                    <div class="card-panel-icon-wrapper">
                        <i class="el-icon-s-comment" style="font-size: 55px"></i>
                    </div>
                    <div class="card-panel-description">
                        <div class="card-panel-text">评论数</div>
                        <span class="card-panel-num">{{ commentCount }}</span>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <el-row class="panel-group" :gutter="20">
            <el-col :span="12">
                <el-card>
                    <div ref="categoryEcharts" style="height:500px;"></div>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card>
                    <div ref="tagEcharts" style="height:500px;"></div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import echarts from 'echarts'
    export default {
        name: "Dashboard",
        data() {
            return {
                pv: 0,
                blogCount: 0,
                commentCount: 0,
                categoryEcharts: null,
                tagEcharts: null,
                categoryOption: {
                    title: {
                        text: '分类下文章数量',
                        x: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c} ({d}%)'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                    },
                    series: {
                        name: '文章数量',
                        type: 'pie',
                        radius: '50%',
                        data: []
                    }
                },
                tagOption: {
                    title: {
                        text: '标签下文章数量',
                        x: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c} ({d}%)'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                    },
                    series: {
                        name: '文章数量',
                        type: 'pie',
                        radius: '50%',
                        data: []
                    }
                },
            }
        },
        created() {
            this.getData()
        },
        methods: {
            getData() {
                this.$axios.get("/dashbord").then(res => {
                    this.pv = res.data.data.pv
                    this.blogCount = res.data.data.blogCount
                    this.commentCount = res.data.data.commentCount

                    //对分类列表进行格式化，去除id，只保留 name 和 value
                    const categoryList = res.data.data.category
                    categoryList.forEach(item => {
                        delete item.id
                    })
                    //赋值给data进行展示
                    this.categoryOption.series.data = categoryList
                    this.initCategoryEcharts()

                    const tagList = res.data.data.tag
                    tagList.forEach(item => {
                        delete item.id
                    })
                    this.tagOption.series.data = tagList
                    this.initTagEcharts()
                })
            },
            initCategoryEcharts() {
                this.categoryEcharts = echarts.init(this.$refs.categoryEcharts, 'light')
                this.categoryEcharts.setOption(this.categoryOption)
            },
            initTagEcharts() {
                this.tagEcharts = echarts.init(this.$refs.tagEcharts, 'light')
                this.tagEcharts.setOption(this.tagOption)
            },
        }
    }
</script>

<style scoped>
    .panel-group {
        margin-bottom: 30px;
    }

    .panel-group .card-panel {
        height: 108px;
        position: relative;
        overflow: hidden;
    }

    .panel-group .card-panel .card-panel-icon-wrapper {
        float: left;
        margin: 14px 0 0 14px;
        padding: 16px;
        margin-top: auto;
    }

    .panel-group .card-panel .card-panel-icon {
        float: left;
        font-size: 48px;
    }

    .panel-group .card-panel .card-panel-description {
        float: right;
        font-weight: 700;
        margin: 26px 26px 26px 0;
        margin-top: 10px;
    }

    .panel-group .card-panel .card-panel-description .card-panel-text {
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 10px;
    }

    .panel-group .card-panel .card-panel-description .card-panel-num {
        font-size: 20px;
    }
</style>