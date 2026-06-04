<template>
	<div class="tag-manage-wrapper">
		<div class="tag-manage-inner">
			<!-- 导航栏 -->
			<el-breadcrumb class="tag-breadcrumb" separator-class="el-icon-arrow-right">
				<el-breadcrumb-item :to="{ name: 'Home' }">首页</el-breadcrumb-item>
				<el-breadcrumb-item>标签管理</el-breadcrumb-item>
			</el-breadcrumb>

			<!-- 操作栏 -->
			<div class="toolbar">
				<el-button type="primary" size="small" icon="el-icon-plus" @click="handleCreate">新建标签</el-button>
			</div>

			<!-- 标签表格 -->
			<el-table
				class="tag-table"
				:data="tagList"
				v-loading="loading"
				size="medium"
			>
				<el-table-column label="颜色" width="80" align="center">
					<template slot-scope="scope">
						<span
							class="tag-color-dot"
							:style="{ background: scope.row.tagColor }"
						></span>
					</template>
				</el-table-column>
				<el-table-column label="标签名称" prop="tagName">
					<template slot-scope="scope">
						<span v-if="scope.row.tagId !== editingTagId">{{ scope.row.tagName }}</span>
						<el-input
							v-else
							v-model="editForm.tagName"
							size="small"
							style="width: 160px"
						></el-input>
					</template>
				</el-table-column>
				<el-table-column label="颜色值" prop="tagColor" width="140" align="center">
					<template slot-scope="scope">
						<span v-if="scope.row.tagId !== editingTagId">{{ scope.row.tagColor }}</span>
						<el-color-picker
							v-else
							v-model="editForm.tagColor"
							size="small"
						></el-color-picker>
					</template>
				</el-table-column>
				<el-table-column label="创建时间" prop="createTime" width="180" align="center"></el-table-column>
				<el-table-column label="操作" width="200" align="center">
					<template slot-scope="scope">
						<template v-if="scope.row.tagId !== editingTagId">
							<el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
							<el-popconfirm
								title="确定删除该标签吗？关联该标签的文件不受影响。"
								@confirm="handleDelete(scope.row)"
							>
								<el-button slot="reference" type="text" size="small" style="color: #F56C6C">删除</el-button>
							</el-popconfirm>
						</template>
						<template v-else>
							<el-button type="text" size="small" @click="handleSaveEdit">保存</el-button>
							<el-button type="text" size="small" @click="handleCancelEdit">取消</el-button>
						</template>
					</template>
				</el-table-column>
			</el-table>

			<!-- 新建标签对话框 -->
			<el-dialog
				title="新建标签"
				:visible.sync="createDialogVisible"
				width="400px"
				:close-on-click-modal="false"
			>
				<el-form label-width="80px" @submit.native.prevent>
					<el-form-item label="标签名称">
						<el-input v-model="createForm.tagName" placeholder="输入标签名称" maxlength="20"></el-input>
					</el-form-item>
					<el-form-item label="标签颜色">
						<el-color-picker v-model="createForm.tagColor"></el-color-picker>
					</el-form-item>
				</el-form>
				<div slot="footer">
					<el-button @click="createDialogVisible = false">取消</el-button>
					<el-button type="primary" @click="handleCreateConfirm" :disabled="!createForm.tagName.trim()">确定</el-button>
				</div>
			</el-dialog>
		</div>
	</div>
</template>

<script>
import { getTagList, createTag, updateTag, deleteTag } from '_r/tag.js'

export default {
	name: 'TagManage',
	data() {
		return {
			loading: false,
			tagList: [],
			editingTagId: null,
			editForm: { tagName: '', tagColor: '' },
			createDialogVisible: false,
			createForm: { tagName: '', tagColor: '#409EFF' }
		}
	},
	mounted() {
		this.loadTags()
		document.title = `标签管理 - ${this.$config.siteName}`
	},
	methods: {
		/**
		 * 加载标签列表
		 */
		loadTags() {
			this.loading = true
			getTagList().then((res) => {
				if (res.success) {
					this.tagList = res.dataList || []
				}
				this.loading = false
			})
		},
		/**
		 * 点击编辑按钮
		 */
		handleEdit(row) {
			this.editingTagId = row.tagId
			this.editForm.tagName = row.tagName
			this.editForm.tagColor = row.tagColor
		},
		/**
		 * 保存编辑
		 */
		handleSaveEdit() {
			if (!this.editForm.tagName.trim()) {
				this.$message.warning('标签名称不能为空')
				return
			}
			updateTag({
				tagId: this.editingTagId,
				tagName: this.editForm.tagName.trim(),
				tagColor: this.editForm.tagColor
			}).then((res) => {
				if (res.success) {
					this.$message.success('修改成功')
					this.editingTagId = null
					this.loadTags()
				} else {
					this.$message.error(res.message)
				}
			})
		},
		/**
		 * 取消编辑
		 */
		handleCancelEdit() {
			this.editingTagId = null
		},
		/**
		 * 删除标签
		 */
		handleDelete(row) {
			deleteTag(row.tagId).then((res) => {
				if (res.success) {
					this.$message.success('删除成功')
					this.loadTags()
				} else {
					this.$message.error(res.message)
				}
			})
		},
		/**
		 * 打开新建对话框
		 */
		handleCreate() {
			this.createForm.tagName = ''
			this.createForm.tagColor = '#409EFF'
			this.createDialogVisible = true
		},
		/**
		 * 确认新建
		 */
		handleCreateConfirm() {
			createTag({
				tagName: this.createForm.tagName.trim(),
				tagColor: this.createForm.tagColor
			}).then((res) => {
				if (res.success) {
					this.$message.success('创建成功')
					this.createDialogVisible = false
					this.loadTags()
				} else {
					this.$message.error(res.message)
				}
			})
		}
	}
}
</script>

<style lang="stylus" scoped>
.tag-manage-wrapper {
	display: block !important;
	padding: 16px 0;
	.tag-manage-inner {
		margin: 0 auto;
		max-width: 900px;
		.tag-breadcrumb {
			margin-bottom: 16px;
		}
		.toolbar {
			margin-bottom: 16px;
			text-align: right;
		}
		.tag-table {
			.tag-color-dot {
				display: inline-block;
				width: 14px;
				height: 14px;
				border-radius: 50%;
				vertical-align: middle;
			}
		}
	}
}
</style>
