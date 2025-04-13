export const passwords: {
  client: {
    [key: string]: string
  }
  employee: {
    [key: string]: string
  }
} = {
  client: {
    'heitor@gmail.com': '1234',
  },
  employee: {
    'italo@empresa.com': '1234',
    'jully@empresa.com': '1234',
  },
}

export const addClientPassword = (email: string, password: string): void => {
  passwords.client[email] = password;
}

export const addEmployeePassword = (email: string, password: string): void => {
  passwords.employee[email] = password;
}
export const updateEmail: (oldEmail: string, newEmail: string, userType: 'client' | 'employee') => void = (oldEmail, newEmail, userType) => {
  const userPasswords = passwords[userType];
  if (userPasswords[oldEmail]) {
    userPasswords[newEmail] = userPasswords[oldEmail];
    delete userPasswords[oldEmail];
  } else {
    throw new Error(`Email ${oldEmail} não encontrado.`);
  }
}