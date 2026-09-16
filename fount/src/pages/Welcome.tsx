import { PageContainer } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import { Card, Col, Row, Space, Typography } from 'antd';
import React from 'react';

const { Paragraph, Text, Link } = Typography;

/**
 * @name 常用文档入口
 */
const docs = [
  {
    title: 'Umi Max',
    href: 'https://umijs.org/docs/max/introduce',
    desc: '应用框架，提供路由、构建、请求、数据流、权限等能力。',
  },
  {
    title: 'Ant Design',
    href: 'https://ant.design/docs/react/introduce-cn',
    desc: 'UI 组件库，antd 6.x 默认使用 CSS-in-JS 主题方案。',
  },
  {
    title: 'Pro Components',
    href: 'https://procomponents.ant.design',
    desc: '高层模板组件：ProTable / ProForm / ProLayout 等。',
  },
  {
    title: 'Ant Design Pro',
    href: 'https://pro.ant.design/zh-CN',
    desc: '本项目的上游模板，完整示例可在其官网查看。',
  },
];

/**
 * @name 常用命令
 */
const commands = [
  { cmd: 'npm run start', desc: '本地开发，接口走 mock 目录' },
  { cmd: 'npm run build', desc: '产出静态文件到 dist' },
  { cmd: 'npm run preview', desc: '本地预览 build 产物' },
  { cmd: 'npm run tsc', desc: 'TypeScript 类型检查' },
  { cmd: 'npm run biome', desc: '代码格式化与 lint 自动修复' },
];

const preStyle: React.CSSProperties = {
  margin: 0,
  padding: 12,
  borderRadius: 6,
  background: 'rgba(150, 151, 153, 0.1)',
  fontSize: 13,
  lineHeight: 1.9,
};

const Welcome: React.FC = () => {
  const { initialState } = useModel('@@initialState');
  const username = initialState?.currentUser?.name ?? '朋友';

  return (
    <PageContainer
      title={`欢迎使用 Fount，${username}`}
      content="这是 Ant Design Pro v6 的精简版：保留登录、权限、ProTable 与布局能力，去掉了示例页、国际化与图表依赖。"
    >
      <Row gutter={[16, 16]}>
        <Col xs={24} lg={14}>
          <Card title="常用命令" variant="borderless">
            <pre style={preStyle}>
              {commands
                .map((item) => `${item.cmd.padEnd(18)}# ${item.desc}`)
                .join('\n')}
            </pre>
            <Paragraph style={{ marginTop: 16, marginBottom: 0 }}>
              <Text type="secondary">
                路由配置在 <Text code>config/routes.ts</Text>，运行时布局、初始状态与请求配置在{' '}
                <Text code>src/app.tsx</Text>，权限在 <Text code>src/access.ts</Text>。
              </Text>
            </Paragraph>
          </Card>
        </Col>
        <Col xs={24} lg={10}>
          <Card title="参考文档" variant="borderless">
            <Space direction="vertical" size={16} style={{ width: '100%' }}>
              {docs.map((item) => (
                <div key={item.href}>
                  <Link href={item.href} target="_blank" rel="noopener noreferrer">
                    {item.title}
                  </Link>
                  <Paragraph type="secondary" style={{ marginBottom: 0, fontSize: 13 }}>
                    {item.desc}
                  </Paragraph>
                </div>
              ))}
            </Space>
          </Card>
        </Col>
      </Row>
    </PageContainer>
  );
};

export default Welcome;
