import { BookOutlined } from '@ant-design/icons';
import { history } from '@umijs/max';
import { Button, Tooltip } from 'antd';
import React from 'react';
import useHeaderActionStyles from './style';

/**
 * @name 顶部导航右侧的操作，国际化切换、版本切换已随精简一并移除
 */
export const DocLink: React.FC = () => {
  const { styles } = useHeaderActionStyles();
  return (
    <Tooltip title="使用文档">
      <Button
        type="text"
        className={styles.action}
        icon={<BookOutlined />}
        aria-label="使用文档"
        onClick={() => {
          history.push('/welcome');
        }}
      />
    </Tooltip>
  );
};
