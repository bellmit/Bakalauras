import { registrationErrors } from '../constants';

export const emailValidation = email => {
  if (email.length !== 0) {
    if (email.indexOf('@') === -1 || email.indexOf('.') === -1 || email.length > 128) {
      return registrationErrors.validEmail;
    }
  } else {
    return registrationErrors.isRequiredPassword;
  }
  return '';
};

export const passwordValidation = password => {
  if (password.length !== 0) {
    if (password.length < 8 || password.length > 255) {
      return registrationErrors.passwordLength;
    }
  } else {
    return registrationErrors.isRequiredPassword;
  }
  return '';
};

export const confirmPasswordValidation = (password, confirmPassword) => {
  if (confirmPassword.length !== 0) {
    if (confirmPassword !== password) {
      return registrationErrors.confirmPasswordLengthError;
    }
  } else {
    return registrationErrors.isRequiredPassword;
  }
  return '';
};

export const usernameValidation = username => {
  if (username.length !== 0) {
    if (username.length < 6 || username.length > 255) {
      return registrationErrors.usernameLength;
    }
  } else {
    return registrationErrors.isRequiredUsername;
  }
  return '';
};
