import { uploadFileUsingPOST } from '@/services/backend/fileController';
import { InboxOutlined } from '@ant-design/icons';
import { Upload, UploadFile, UploadProps, message } from 'antd';
import React, { useState } from 'react';

const { Dragger } = Upload;

interface FileUploaderProps {
  biz: string;
  onChange?: (fileList: UploadFile[]) => void;
  value?: UploadFile[];
  description?: string;
}

const FileUploader: React.FC<FileUploaderProps> = (props) => {
  const { biz, onChange, value, description } = props;
  const [loading, setLoading] = useState(false);
  const uploadProps: UploadProps = {
    name: 'file',
    multiple: false,
    maxCount: 1,
    listType: 'text',
    fileList: value,
    disabled: loading,
    onChange: ({ fileList }) => {
      onChange?.(fileList);
    },
    customRequest: async (fileObj: any) => {
      setLoading(true);
      try {
        const res = await uploadFileUsingPOST({ biz }, {}, fileObj.file);
        fileObj.onSuccess(res.data);
      } catch (e) {
        message.error('上传失败');
        fileObj.onError(e);
      }
      setLoading(false);
    },
  };

  return (
    <Dragger {...uploadProps}>
      <p className="ant-upload-drag-icon">
        <InboxOutlined />
      </p>
      <p className="ant-upload-text">点击或拖拽文件上传</p>
      <p className="ant-upload-hint">{description || ''}</p>
    </Dragger>
  );
};
export default FileUploader;
