// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addGenerator POST /api/generator/add */
export async function addGeneratorUsingPOST(
  body: API.GeneratorAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/generator/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** cacheGenerator POST /api/generator/cache */
export async function cacheGeneratorUsingPOST(
  body: API.GeneratorCacheRequest,
  options?: { [key: string]: any },
) {
  return request<any>('/api/generator/cache', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteGenerator POST /api/generator/delete */
export async function deleteGeneratorUsingPOST(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/generator/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** downloadGeneratorById GET /api/generator/download */
export async function downloadGeneratorByIdUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.downloadGeneratorByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<any>('/api/generator/download', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** editGenerator POST /api/generator/edit */
export async function editGeneratorUsingPOST(
  body: API.GeneratorEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/generator/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getGeneratorVOById GET /api/generator/get/vo */
export async function getGeneratorVOByIdUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getGeneratorVOByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseGeneratorVO_>('/api/generator/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listGeneratorByPage POST /api/generator/list/page */
export async function listGeneratorByPageUsingPOST(
  body: API.GeneratorQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageGenerator_>('/api/generator/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listGeneratorVOByPage POST /api/generator/list/page/vo */
export async function listGeneratorVOByPageUsingPOST(
  body: API.GeneratorQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageGeneratorVO_>('/api/generator/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listGeneratorVOByPageFast POST /api/generator/list/page/vo/fast */
export async function listGeneratorVOByPageFastUsingPOST(
  body: API.GeneratorQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageGeneratorVO_>('/api/generator/list/page/vo/fast', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** makeGenerator POST /api/generator/make */
export async function makeGeneratorUsingPOST(
  body: API.GeneratorMakeRequest,
  options?: { [key: string]: any },
) {
  return request<any>('/api/generator/make', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listMyGeneratorVOByPage POST /api/generator/my/list/page/vo */
export async function listMyGeneratorVOByPageUsingPOST(
  body: API.GeneratorQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageGeneratorVO_>('/api/generator/my/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateGenerator POST /api/generator/update */
export async function updateGeneratorUsingPOST(
  body: API.GeneratorUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/generator/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** useGenerator POST /api/generator/use */
export async function useGeneratorUsingPOST(
  body: API.GeneratorUseRequest,
  options?: { [key: string]: any },
) {
  return request<any>('/api/generator/use', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
