package cn.matio.interview_internal_reference.ali1;

/**
 * /01.阿里篇/1.1.6 从innodb的索引结构分析，为什么索引的 key 长度不能太长.md
 * 题目：从 innodb 的索引结构分析，为什么索引的 key 长度不能太长？
 *
 * 参考答案：key 太长会导致一个页当中能够存放的 key 的数目变少，间接导致索引树的页数目变多，索引层次增加，从而影响整体查询变更的效率。
 *
 */
public class Ali6 {
}
