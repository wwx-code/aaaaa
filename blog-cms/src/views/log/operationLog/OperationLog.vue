<template>
    <div>
        <!--面包屑导航-->
        <Breadcrumb parentTitle="日志管理"/>

        <!--搜索-->
        <el-form inline>
            <el-form-item label="操作时间">
                <DateTimeRangePicker :date="queryInfo.date" :setDate="setDate"/>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" size="small" icon="el-icon-search" @click="search">搜索</el-button>
            </el-form-item>
        </el-form>

        <el-table :data="logList">
            <el-table-column label="序号" type="index" width="50"></el-table-column>
            <el-table-column label="操作者" prop="username"></el-table-column>
            <el-table-column label="请求方式" prop="method">
                <template v-slot="scope">
                    <el-popover trigger="hover" placement="top">
                        <p>请求接口: {{ scope.row.uri }}</p>
                        <p>请求参数: {{ scope.row.param }}</p>
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="small">{{ scope.row.method }}</el-tag>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column label="描述" prop="description"></el-table-column>
            <el-table-column label="ip" prop="ip"></el-table-column>
            <el-table-column label="ip来源" prop="ipSource" show-overflow-tooltip></el-table-column>
            <el-table-column label="操作系统" prop="os"></el-table-column>
            <el-table-column label="浏览器" prop="browser" show-overflow-tooltip></el-table-column>
            <el-table-column label="操作耗时" prop="times">
                <template v-slot="scope">
                    <el-tag size="small">{{ scope.row.times }}ms</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作时间" width="170">
                <template v-slot="scope">{{ scope.row.createTime }}</template>
            </el-table-column>
            <el-table-column label="操作" width="120">
                <template v-slot="scope">
                    <el-popconfirm title="确定删除吗？" icon="el-icon-delete" iconColor="red" @confirm="deleteLogById(scope.row.id)">
                        <el-button size="mini" type="danger" icon="el-icon-delete" slot="reference">删除</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <!--分页-->
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryInfo.pageNum"
                       :page-sizes="[10, 20, 30, 50]" :page-size="queryInfo.pageSize" :total="total"
                       layout="total, sizes, prev, pager, next, jumper" background>
        </el-pagination>
    </div>
</template>

<script>
    import Breadcrumb from "@/components/Breadcrumb";
    import DateTimeRangePicker from "@/components/DateTimeRangePicker";
    import {getOperationLogList, deleteOperationLogById} from "@/api/operationLog";
    export default {
        name: "OperationLog",
        components: {DateTimeRangePicker, Breadcrumb},
        data() {
            return {
                queryInfo: {
                    date: [],
                    pageNum: 1,
                    pageSize: 10
                },
                logList: [],
                total: 0
            }
        },
        created() {
            this.getData()
        },
        methods: {
            getData() {
                let query = {...this.queryInfo}
                if (query.date && query.date.length == 2){
                    query.date = query.date[0] + ',' + query.date[1]
                }
                getOperationLogList(query).then(res => {
                    this.msgSuccess(res.data.msg)
                    this.logList = res.data.data.records
                    this.total = res.data.data.total
                })
            },
            handleSizeChange(newSize) {
                this.queryInfo.pageSize = newSize
                this.getData()
            },
            handleCurrentChange(newPage) {
                this.queryInfo.pageNum = newPage
                this.getData()
            },
            deleteLogById(id) {
                deleteOperationLogById(id).then(res => {
                    this.msgSuccess(res.data.msg)
                    this.getData()
                })
            },
            search() {
                this.queryInfo.pageNum = 1
                this.queryInfo.pageSize = 10
                this.getData()
            },
            setDate(value) {
                this.queryInfo.date = value
            },
        }
    }
</script>

<style scoped>

</style>