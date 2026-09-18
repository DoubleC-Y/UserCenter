// @ts-ignore
/* eslint-disable */

declare namespace API {
  type CurrentUser = {
    name?: string;
    avatar?: string;
    userid?: string;
    email?: string;
    signature?: string;
    title?: string;
    group?: string;
    tags?: { key?: string; label?: string }[];
    notifyCount?: number;
    unreadCount?: number;
    country?: string;
    access?: string;
    geographic?: {
      province?: { label?: string; key?: string };
      city?: { label?: string; key?: string };
    };
    address?: string;
    phone?: string;
  };

  /**
   * 登录接口 POST /user/login 的返回体
   * 后端已对 User 做脱敏：只回传可外发字段，不含 password / updateTime / deleted
   */
  type LoginResult = {
    /** 主键ID */
    id?: number;
    /** 姓名 */
    name?: string;
    /** 登录账号：仅数字 */
    account?: string;
    /** 年龄 */
    age?: number;
    /** 邮箱 */
    email?: string;
    /** 手机号 */
    phone?: string;
    /** 头像URL */
    avatar?: string;
    /** 个人简介 */
    intro?: string;
    /** 是否启用：1启用，0停用 */
    enabled?: number;
    /** 人员状态：1在职，2离职 */
    workStatus?: number;
    /** 角色：0普通用户，1管理员 */
    role?: number;
    /** 创建时间 */
    createTime?: string;
  };

  type PageParams = {
    current?: number;
    pageSize?: number;
  };

  type RuleListItem = {
    key?: number;
    disabled?: boolean;
    href?: string;
    avatar?: string;
    name?: string;
    owner?: string;
    desc?: string;
    callNo?: number;
    status?: number;
    updatedAt?: string;
    createdAt?: string;
    progress?: number;
  };

  type RuleList = {
    data?: RuleListItem[];
    /** 列表的内容总数 */
    total?: number;
    success?: boolean;
  };

  type FakeCaptcha = {
    code?: number;
    status?: string;
  };

  /**
   * 登录接口 POST /user/login 的请求体
   * 字段名与后端 UserLoginRequest 保持一致
   */
  type LoginParams = {
    /** 用户账号 */
    userAccount?: string;
    /** 用户密码 */
    userPassword?: string;
  };

  type ErrorResponse = {
    /** 业务约定的错误码 */
    errorCode: string;
    /** 业务上的错误信息 */
    errorMessage?: string;
    /** 业务上的请求是否成功 */
    success?: boolean;
  };

  type NoticeIconList = {
    data?: NoticeIconItem[];
    /** 列表的内容总数 */
    total?: number;
    success?: boolean;
  };

  type NoticeIconItemType = 'notification' | 'message' | 'event';

  type NoticeIconItem = {
    id?: string;
    extra?: string;
    key?: string;
    read?: boolean;
    avatar?: string;
    title?: string;
    status?: string;
    datetime?: string;
    description?: string;
    type?: NoticeIconItemType;
  };
}
