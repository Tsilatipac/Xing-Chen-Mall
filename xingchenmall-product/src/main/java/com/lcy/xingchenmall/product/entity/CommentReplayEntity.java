package com.lcy.xingchenmall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ÉÌÆ·ÆÀ¼Û»Ø¸´¹ØÏµ
 * 
 * @author lichenyu
 * @email 1596180765@qq.com
 * @date 2020-06-01 14:29:04
 */
@Data
@TableName("pms_comment_replay")
public class CommentReplayEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 评论id
	 */
	private Long commentId;
	/**
	 * 回复id
	 */
	private Long replyId;

}
