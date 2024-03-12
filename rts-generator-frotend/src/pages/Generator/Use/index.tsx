import {
  getGeneratorVOByIdUsingGET,
  useGeneratorUsingPOST,
} from '@/services/backend/generatorController';
import { Link, useModel, useParams } from '@@/exports';
import { DownloadOutlined } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
import {
  Button,
  Card,
  Col,
  Collapse,
  Form,
  Image,
  Input,
  message,
  Row,
  Space,
  Tag,
  Typography,
} from 'antd';
import { saveAs } from 'file-saver';
import React, { useEffect, useState } from 'react';

const GeneratorUse: React.FC = () => {
  const { id } = useParams();
  const [form] = Form.useForm();

  const [loading, setLoading] = useState<boolean>(false);
  const [downloading, setDownloading] = useState<boolean>(false);
  const [data, setData] = useState<API.GeneratorVO>({});
  const { initialState } = useModel('@@initialState');
  const { currentUser } = initialState ?? {};

  const models = data?.modelConfig?.models ?? [];

  const getGeneratorInfoById = async () => {
    if (!id) return;

    setLoading(true);
    try {
      const res = await getGeneratorVOByIdUsingGET({
        id,
      });
      setData(res.data || {});
    } catch (error: any) {
      message.error('获取数据失败，' + error.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    getGeneratorInfoById();
  }, [id]);

  const tagListView = (tags?: string[]) => {
    if (!tags) return <></>;

    return (
      <div style={{ marginBottom: 8 }}>
        {tags.map((tag: string) => {
          return <Tag key={tag}>{tag}</Tag>;
        })}
      </div>
    );
  };

  const downloadButton = data.distPath && currentUser && (
    <Button
      type="primary"
      icon={<DownloadOutlined />}
      loading={downloading}
      onClick={async () => {
        setDownloading(true);
        const values = form.getFieldsValue();

        // eslint-disable-next-line react-hooks/rules-of-hooks
        const blob = await useGeneratorUsingPOST(
          {
            id: data.id,
            dataModel: values,
          },
          {
            responseType: 'blob',
          },
        );

        const fullPath = data.distPath || '';
        saveAs(blob, fullPath.substring(fullPath.lastIndexOf('/') + 1));
        setDownloading(false);
      }}
    >
      生成代码
    </Button>
  );

  return (
    <PageContainer title={<></>} loading={loading}>
      <Card>
        <Row justify="space-between" gutter={[32, 32]}>
          <Col flex="auto">
            <Space size="large" align="center">
              <Typography.Title level={4}>{data.name}</Typography.Title>
              {tagListView(data.tags)}
            </Space>
            <Typography.Paragraph>{data.description}</Typography.Paragraph>
            <div style={{ marginBottom: 24 }} />
            <Form form={form}>
              {models.map((model, index) => {
                // 分组
                if (model.groupKey) {
                  if (!model.models) return <></>;

                  return (
                    <Collapse
                      key={index}
                      style={{
                        marginBottom: 24,
                      }}
                      items={[
                        {
                          key: index,
                          label: model.groupName + '（分组）',
                          children: model.models.map((subModel, index) => {
                            return (
                              <Form.Item
                                key={index}
                                label={subModel.fieldName}
                                // @ts-ignore
                                name={[model.groupKey, subModel.fieldName]}
                              >
                                <Input placeholder={subModel.description} />
                              </Form.Item>
                            );
                          }),
                        },
                      ]}
                      bordered={false}
                      defaultActiveKey={[index]}
                    />
                  );
                }

                return (
                  <Form.Item label={model.fieldName} name={model.fieldName} key={index}>
                    <Input placeholder={model.description} />
                  </Form.Item>
                );
              })}
            </Form>
            <Space size="middle">
              {downloadButton}
              <Link to={`/generator/detail/${id}`}>
                <Button>查看详情</Button>
              </Link>
            </Space>
          </Col>
          <Col flex="320px">
            <Image src={data.picture} />
          </Col>
        </Row>
      </Card>
    </PageContainer>
  );
};

export default GeneratorUse;
