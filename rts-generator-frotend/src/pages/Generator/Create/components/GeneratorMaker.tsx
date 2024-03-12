import FileUploader from '@/components/FileUploader';
import { makeGeneratorUsingPOST } from '@/services/backend/generatorController';
import { ProForm, ProFormInstance, ProFormItem } from '@ant-design/pro-components';
import { Collapse, message } from 'antd';
import saveAs from 'file-saver';
import { useRef } from 'react';

interface GeneratorMakerProps {
  meta: any;
}

const GeneratorMaker: React.FC<GeneratorMakerProps> = (props) => {
  const { meta } = props;
  const formRef = useRef<ProFormInstance>();

  const onSubmit = async (values: API.GeneratorMakeRequest) => {
    if (!meta.name) {
      message.error('请填写名称');
      return;
    }

    const zipFilePath = values.zipFilePath;
    if (!zipFilePath || zipFilePath.length < 1) {
      message.error('请上传模板文件压缩包');
      return;
    }

    // 文件列表转换成 url
    // @ts-ignore
    values.zipFilePath = zipFilePath[0].response;

    try {
      const blob = await makeGeneratorUsingPOST(
        {
          meta,
          zipFilePath: values.zipFilePath,
        },
        {
          responseType: 'blob',
        },
      );
      // 使用 file-saver 来保存文件
      saveAs(blob, meta.name + '.zip');
    } catch (error: any) {
      message.error('制作失败，' + error.message);
    }
  };

  const temUpForm = () => {
    return (
      <ProForm
        formRef={formRef}
        submitter={{
          searchConfig: {
            submitText: '制作',
          },
          resetButtonProps: {
            hidden: true,
          },
        }}
        onFinish={onSubmit}
      >
        <ProFormItem label="模板文件" name="zipFilePath">
          <FileUploader
            biz="generator_make_template"
            description="请上传压缩包，打包时不要添加最外层目录！"
          />
        </ProFormItem>
      </ProForm>
    );
  };

  return (
    <Collapse
      style={{
        marginBottom: 24,
      }}
      items={[
        {
          key: 'maker',
          label: '生成器制作工具',
          children: temUpForm(),
        },
      ]}
    />
  );
};

export default GeneratorMaker;
