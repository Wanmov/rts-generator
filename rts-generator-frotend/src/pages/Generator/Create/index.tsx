import FileUploader from '@/components/FileUploader';
import PictureUploader from '@/components/PictureUploader';
import {
  addGeneratorUsingPOST,
  editGeneratorUsingPOST,
  getGeneratorVOByIdUsingGET,
} from '@/services/backend/generatorController';
import type { ProFormInstance } from '@ant-design/pro-components';
import {
  ProCard,
  ProFormItem,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  StepsForm,
} from '@ant-design/pro-components';
import { UploadFile, message } from 'antd';
import { useEffect, useRef, useState } from 'react';
import { history, useSearchParams } from '@umijs/max';
import { COS_HOST } from '@/constants';

const GeneratorCreate: React.FC = () => {
  const [searchParams] = useSearchParams();
  const generatorId = searchParams.get('id');
  const [oldData, setOldData] = useState<API.GeneratorEditRequest>();
  const formRef = useRef<ProFormInstance>();

  const getDataById = async () => {
    if (!generatorId) return;
    try {
      const res = await getGeneratorVOByIdUsingGET({ id: generatorId });
      // 处理文件路径
      if (res.data) {
        const { distPath } = res.data ?? {};
        if (distPath) {
          //@ts-ignore
          res.data.distPath = [
            {
              uid: generatorId,
              name: '文件' + generatorId,
              status: 'done',
              url: COS_HOST + distPath,
              response: distPath,
            } as UploadFile,
          ];
        }
        setOldData(res.data);
      }
    } catch (error: any) {
      message.error('加载数据失败，' + error.message);
    }
  };

  useEffect(() => {
    if (!generatorId) return;
    getDataById();
  }, [generatorId]);

  const AddGenerator = async (values: API.GeneratorAddRequest) => {
    try {
      const res = await addGeneratorUsingPOST(values);
      if (res.data) {
        message.success('创建成功');
        history.push(`/generator/detail/${res.data}`);
      }
    } catch (error: any) {
      message.error('创建失败，' + error.message);
    }
  };

  const UpdataGenerator = async (values: API.GeneratorEditRequest) => {
    try {
      const res = await editGeneratorUsingPOST(values);
      if (res.data) {
        message.success('更新成功');
        history.push(`/generator/detail/${generatorId}`);
      }
    } catch (error: any) {
      message.error('更新失败，' + error.message);
    }
  };

  const onSubmit = async (values: API.GeneratorAddRequest) => {
    // 数据转换
    if (!values.fileConfig) values.fileConfig = {};
    if (!values.modelConfig) values.modelConfig = {};
    // 文件列表转换url
    if (values.distPath && values.distPath.length > 0) {
      //@ts-ignore
      values.distPath = values.distPath[0].response;
    }

    if (generatorId) await UpdataGenerator({ ...values, id: +generatorId });
    await AddGenerator(values);
  };
  return (
    <ProCard>
      {(!generatorId || oldData) && (
        <StepsForm<API.GeneratorAddRequest> formRef={formRef} onFinish={onSubmit}>
          <StepsForm.StepForm
            name="basicInfo"
            title="基本信息"
            onFinish={async () => {
              console.log(formRef.current?.getFieldsValue());
              return true;
            }}
          >
            <ProFormText name="name" label="名称" placeholder="请输入名称" />
            <ProFormTextArea name="description" label="描述" placeholder="请输入描述" />
            <ProFormText name="basePackage" label="基础包" placeholder="请输入基础包" />
            <ProFormText name="version" label="版本" placeholder="请输入版本" />
            <ProFormText name="author" label="作者" placeholder="请输入作者" />
            <ProFormSelect label="标签" mode="tags" name="tags" placeholder="请输入标签列表" />
            <ProFormItem label="图片" name="picture">
              <PictureUploader biz="generator_picture" />
            </ProFormItem>
          </StepsForm.StepForm>
          <StepsForm.StepForm name="fileConfig" title="文件配置">
            {/* todo */}
          </StepsForm.StepForm>
          <StepsForm.StepForm name="modelConfig" title="模型配置">
            {/* todo */}
          </StepsForm.StepForm>
          <StepsForm.StepForm name="dist" title="生成器文件">
            <ProFormItem label="产物包" name="distPath">
              <FileUploader biz="generator_dist" description="请上传生成器文件压缩包" />
            </ProFormItem>
          </StepsForm.StepForm>
        </StepsForm>
      )}
    </ProCard>
  );
};

export default GeneratorCreate;
