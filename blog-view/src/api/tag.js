import axios from "axios";

export function getBlogListByTagName(pageNum, tagName) {
    return axios({
        url: "tagName",
        method: "GET",
        params: {
            pageNum,
            tagName
        }
    })
}