<template>
	<!-- 编辑文件标签对话框 -->
	<el-dialog
		title="编辑标签"
		:visible.sync="visible"
		:close-on-click-modal="false"
		width="550px"
		@open="handleDialogOpen"
		@close="handleDialogClose"
	>
		<div class="tag-edit-wrapper">
			<!-- 已添加的标签 -->
			<div class="current-tags">
				<span class="label-text">当前标签：</span>
				<el-tag
					v-for="tag in fileTags"
					:key="tag.tagId"
					:color="tag.tagColor"
					:closable="true"
					@close="handleRemoveTag(tag)"
					size="medium"
					effect="dark"
					style="margin: 0 6px 6px 0; color: #fff;"
				>
					{{ tag.tagName }}
				</el-tag>
				<span v-if="fileTags.length === 0" class="no-tag-text">暂无标签</span>
			</div>
			<!-- 添加标签区域 -->
			<div class="add-tag-section">
				<el-select
					v-model="selectedTagId"
					placeholder="选择已有标签"
					clearable
					style="width: 180px; margin-right: 8px;"
				>
					<el-option
						v-for="tag in availableTags"
						:key="tag.tagId"
						:label="tag.tagName"
						:value="tag.tagId"
					>
						<span class="tag-option">
							<span
								class="tag-color-dot"
								:style="{ background: tag.tagColor }"
							></span>
							{{ tag.tagName }}
						</span>
					</el-option>
				</el-select>
				<el-button
					type="primary"
					size="small"
					@click="handleAddSelectedTag"
					:disabled="!selectedTagId"
					icon="el-icon-plus"
				>添加</el-button>
			</div>
			<!-- 创建新标签 -->
			<div class="create-tag-section">
				<el-input
					v-model="newTagName"
					placeholder="输入新标签名"
					size="small"
					style="width: 180px; margin-right: 8px;"
					maxlength="20"
				></el-input>
				<el-color-picker
					v-model="newTagColor"
					size="small"
					style="margin-right: 8px; vertical-align: middle;"
				></el-color-picker>
				<el-button
					type="success"
					size="small"
					@click="handleCreateAndAddTag"
					:disabled="!newTagName.trim()"
					icon="el-icon-plus"
				>新建标签</el-button>
			</div>
		</div>
		<div slot="footer" class="dialog-footer">
			<el-button @click="handleDialogClose">关 闭</el-button>
		</div>
	</el-dialog>
</template>

<script>
import { getTagList, createTag, getFileTags, addTagToFile, removeTagFromFile } from '_r/tag.js'

export default {
	name: 'TagEditDialog',
	data() {
		return {
			visible: false, //  对话框是否可见
			fileTags: [], //  文件已有标签
			allTags: [], //  用户所有标签
			selectedTagId: null, //  选中的待添加标签id
			newTagName: '', //  新标签名称
			newTagColor: '#409EFF' //  新标签颜色（默认蓝色）
		}
	},
	computed: {
		// 可选标签（排除已添加的）
		availableTags() {
			const fileTagIds = this.fileTags.map(t => t.tagId)
			return this.allTags.filter(t => !fileTagIds.includes(t.tagId))
		}
	},
	methods: {
		/**
		 * 对话框打开时的回调
		 */
		handleDialogOpen() {
			this.loadFileTags()
			this.loadAllTags()
			this.selectedTagId = null
			this.newTagName = ''
			this.newTagColor = '#409EFF'
		},
		/**
		 * 对话框关闭时的回调
		 */
		handleDialogClose() {
			this.visible = false
			this.callback('cancel')
		},
		/**
		 * 加载文件已有标签
		 */
		loadFileTags() {
			getFileTags(this.userFileId).then((res) => {
				if (res.success) {
					this.fileTags = res.dataList || []
				}
			})
		},
		/**
		 * 加载用户所有标签
		 */
		loadAllTags() {
			getTagList().then((res) => {
				if (res.success) {
					this.allTags = res.dataList || []
				}
			})
		},
		/**
		 * 从选中的标签添加到文件
		 */
		handleAddSelectedTag() {
			if (!this.selectedTagId) return
			addTagToFile({
				userFileId: this.userFileId,
				tagIds: [this.selectedTagId]
			}).then((res) => {
				if (res.success) {
					this.selectedTagId = null
					this.loadFileTags()
					this.loadAllTags()
					this.callback('confirm')
				} else {
					this.$message.error(res.message)
				}
			})
		},
		/**
		 * 创建新标签并添加到文件
		 */
		handleCreateAndAddTag() {
			const tagName = this.newTagName.trim()
			if (!tagName) return
			createTag({
				tagName: tagName,
				tagColor: this.newTagColor
			}).then((res) => {
				if (res.success) {
					const newTag = res.data
					// 添加到文件
					addTagToFile({
						userFileId: this.userFileId,
						tagIds: [newTag.tagId]
					}).then((addRes) => {
						if (addRes.success) {
							this.newTagName = ''
							this.loadFileTags()
							this.loadAllTags()
							this.callback('confirm')
						}
					})
				} else {
					this.$message.error(res.message)
				}
			})
		},
		/**
		 * 从文件移除标签
		 */
		handleRemoveTag(tag) {
			removeTagFromFile({
				userFileId: this.userFileId,
				tagId: tag.tagId
			}).then((res) => {
				if (res.success) {
					this.loadFileTags()
					this.loadAllTags()
					this.callback('confirm')
				} else {
					this.$message.error(res.message)
				}
			})
		}
	}
}
</script>

<style lang="stylus" scoped>
.tag-edit-wrapper {
	padding: 0 8px;

	.current-tags {
		margin-bottom: 20px;
		min-height: 36px;

		.label-text {
			font-size: 14px;
			color: #606266;
			margin-right: 8px;
		}

		.no-tag-text {
			color: #C0C4CC;
			font-size: 13px;
		}
	}

	.add-tag-section {
		margin-bottom: 16px;
		display: flex;
		align-items: center;
	}

	.create-tag-section {
		padding-top: 16px;
		border-top: 1px solid #EBEEF5;
		display: flex;
		align-items: center;
	}

	.tag-option {
		display: flex;
		align-items: center;

		.tag-color-dot {
			display: inline-block;
			width: 12px;
			height: 12px;
			border-radius: 50%;
			margin-right: 8px;
		}
	}
}
</style>
