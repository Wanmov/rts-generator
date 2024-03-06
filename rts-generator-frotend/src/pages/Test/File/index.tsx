import { COS_HOST } from '@/constants';
import { testDownloadFileUsingGET, testUploadFileUsingPOST } from '@/services/backend/fileController';
import { InboxOutlined } from '@ant-design/icons';
import { Button, Card, Divider, Flex, Upload, UploadProps, message } from 'antd';
import { saveAs } from 'file-saver';
import React, { useState } from 'react';

const { Dragger } = Upload;

const TestFile: React.FC = () => {
  const [filePath, setFilePath] = useState<string>();

  const props: UploadProps = {
    name: 'file',
    multiple: false,
    maxCount: 1,
    customRequest: async (fileObj: any) => {
      try {
        const res = await testUploadFileUsingPOST({}, fileObj.file);
        fileObj.onSuccess(res.data);
        setFilePath(res.data);
      } catch (e) {
        message.error('上传失败');
        fileObj.onError(e);
      }
    },
    onRemove: (file) => {
      setFilePath(undefined);
    },
  };

  return (
    <Flex gap={16}>
      <Card title="文件上传">
        <Dragger {...props}>
          <p className="ant-upload-drag-icon">
            <InboxOutlined />
          </p>
          <p className="ant-upload-text">Click or drag file to this area to upload</p>
          <p className="ant-upload-hint">
            Support for a single or bulk upload. Strictly prohibited from uploading company data or
            other banned files.
          </p>
        </Dragger>
      </Card>
      <Card title="文件下载">
        <div>文件地址:{COS_HOST + filePath}</div>
        <Divider/>
        <img src={COS_HOST + filePath} height={200} />
        <Button onClick={async()=>{
            const res = await testDownloadFileUsingGET({filepath:filePath},{
                responseType: 'blob',
            });
            //file-saver下载文件
            const fullPath = COS_HOST + filePath
            saveAs(res,filePath?.substring(fullPath.lastIndexOf('/')+1))
        }}>点击下载文件</Button>
      </Card>
    </Flex>
  );
};
export default TestFile;
