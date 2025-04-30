export type ApiSuccess<T> = {
  error: false
  data: T
}

export type ApiError = {
  error: true
  statusCode: number
  message: string
}

export type ApiResponse<T> = ApiSuccess<T> | ApiError
