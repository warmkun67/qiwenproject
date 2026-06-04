// 文件标签相关接口
import { get, post, put, axiosDelete } from './http'

// 获取用户所有标签
export const getTagList = (p) => get('/tag/list', p)
// 创建标签
export const createTag = (p) => post('/tag/create', p)
// 更新标签
export const updateTag = (p) => put('/tag/update', p)
// 删除标签
export const deleteTag = (tagId) => axiosDelete(`/tag/delete/${tagId}`)
// 获取文件的所有标签
export const getFileTags = (userFileId) => get(`/tag/file/${userFileId}`)
// 批量获取文件的标签
export const getBatchTags = (userFileIds) => get('/tag/batch', { userFileIds: userFileIds.join(',') })
// 给文件添加标签
export const addTagToFile = (p) => post('/tag/addToFile', p)
// 从文件移除标签
export const removeTagFromFile = (p) => axiosDelete('/tag/removeFromFile', p)
// 设置文件标签（覆盖式）
export const setFileTags = (p) => post('/tag/setFileTags', p)
