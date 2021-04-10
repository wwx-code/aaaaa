import axios from "axios";

export function getOperationLogList(queryInfo) {
    return axios({
        url: 'operationLogs',
        method: 'GET',
        params: {
            ...queryInfo
        }
    })
}

export function deleteOperationLogById(id) {
    return axios({
        url: 'operationLog/delete',
        method: 'GET',
        params: {
            id
        }
    })
}