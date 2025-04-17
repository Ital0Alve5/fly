import 'dotenv/config'

export class Env {
  static get AUTH_SERVICE_URL(): string {
    return process.env.AUTH_SERVICE_URL
  }

  static get CLIENT_SERVICE_URL(): string {
    return process.env.CLIENT_SERVICE_URL
  }

  static get FLY_SERVICE_URL(): string {
    return process.env.FLY_SERVICE_URL
  }

  static get RESERVATION_SERVICE_URL(): string {
    return process.env.RESERVATION_SERVICE_URL
  }

  static get EMPLOYEE_SERVICE_URL(): string {
    return process.env.EMPLOYEE_SERVICE_URL
  }
}
