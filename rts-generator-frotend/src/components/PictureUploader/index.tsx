import { COS_HOST } from '@/constants';
import { uploadFileUsingPOST } from '@/services/backend/fileController';
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons';
import { Upload, UploadProps, message } from 'antd';
import React, { useState } from 'react';

interface PicUploaderProps {
  biz: string;
  onChange: (url: string) => void;
  value?: string;
}

const PicUploader: React.FC<PicUploaderProps> = (props) => {
  const { biz, onChange, value } = props;
  const [loading, setLoading] = useState(false);
  const uploadProps: UploadProps = {
    name: 'file',
    multiple: false,
    maxCount: 1,
    showUploadList: false,
    listType: 'picture-card',
    disabled: loading,
    customRequest: async (fileObj: any) => {
      setLoading(true);
      try {
        const res = await uploadFileUsingPOST({ biz }, {}, fileObj.file);
        // 拼接完整图片路径
        const fullPath: string = COS_HOST + res.data;
        onChange(fullPath ?? '');
        fileObj.onSuccess(res.data);
      } catch (e) {
        message.error('上传失败');
        fileObj.onError(e);
      }
      setLoading(false);
    },
  };

  const uploadButton = (
    <button style={{ border: 0, background: 'none' }} type="button">
      {loading ? <LoadingOutlined /> : <PlusOutlined />}
      <div style={{ marginTop: 8 }}>上传</div>
    </button>
  );

  return (
    <Upload {...uploadProps}>
      {value ? <img src={value} alt="picture" style={{ width: '100%' }} /> : uploadButton}
    </Upload>
  );
};
export default PicUploader;
