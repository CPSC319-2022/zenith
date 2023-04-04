// api.js

import axios from 'axios';
import { getAccessToken } from './redux/slices/auth';


const getApiUrl = () => {
  return process.env.REACT_APP_BACKEND_URL || 'http://localhost:8080';
};

const apiUrl = getApiUrl();
export const getPosts = async ({ postIDStart, count, reverse }) => {
  try {
    const response = await axios.get(`${apiUrl}/post/gets`, {
      params: {
        postIDStart,
        count,
        reverse,
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const getPost = async ({postID}) => {
    const response = await axios.get(`${apiUrl}/post/get`, {
      params: { postID },
    });
    return response.data;
};

export const getPostsByUser = async ({ userID, count }) => {
  try {
    const response = await axios.get(`${apiUrl}/post/byUser`, {
      params: {
        userID,
        count,
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
}

export const createPost = async ({ title, content, allowComments, image }) => {
  const token = getAccessToken();

  // Create a FormData object
  const formData = new FormData();

  // Append fields to the FormData object
  formData.append('jsonData', JSON.stringify({ title, content, allowComments }));

  // Append the optional image, if provided
  if (image) {
    console.log('image exists');
    formData.append('file', image);
  }

  // Update the request to use FormData and set the content type
  const response = await axios.post(`${apiUrl}/post/create`, formData, {
    headers: {
      Authorization: `Bearer ${token.credential}`,
      'X-Oauth-Provider': 'google',
      'X-Oauth-Credential': JSON.stringify(token.credential),
      'Content-Type': 'multipart/form-data', // Keep content type as 'multipart/form-data'
    },
  });

  return response.data;
};


export const editPost = async ({ postID, title, content, allowComments }) => {
  const token = getAccessToken();
  console.log("editPost token: ", token);
  console.log("typeof token.credential: ", typeof token.credential);
  const response = await axios.put(`${apiUrl}/post/edit`, {
    postID,
    title,
    content,
    allowComments,
  }, {
    headers: {
      Authorization: `Bearer ${token.credential}`,
      'X-Oauth-Provider': 'google',
      'X-Oauth-Credential': JSON.stringify(token.credential),
    },
  });
  return response.data;
};


export const deletePost = async ({ postID }) => {
  const token = getAccessToken();

  try {
    console.log('api.js deletePost: ',{postID});
    const response = await axios.delete(`${apiUrl}/post/delete`, {
      params: { postID },
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });

    if (response.status !== 200) {
      throw new Error('Server Error');
    }

    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const filterPosts = async ({ searchQuery, sortBy }) => {
  const token = getAccessToken();
  try {
    const response = await axios.get(`${apiUrl}/post/search`, {
      params: {
        pattern: searchQuery,
        sortBy: sortBy,
        count: 90,
      },
    });
    console.log("api works", response.data);
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

//Comments
export const getComments = async ({ postID, commentIDStart, count, reverse }) => {
    try {
      const response = await axios.get(`${apiUrl}/comment/gets`, {
        params: {
          postID,
          commentIDStart,
          count,
          reverse,
        },
      });
      return response.data;
    } catch (error) {
      throw new Error(error.message);
    }
};

export const getComment = async ({ postID, commentID }) => {
  const token = getAccessToken();

  const response = await axios.get(`${apiUrl}/comment/get`, {
    params: { postID, commentID },
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token.credential}`,
      'X-Oauth-Provider': 'google',
      'X-Oauth-Credential': JSON.stringify(token.credential),
    },
  });
  return response.data;
};

export const createComment = async ({ postID, content }) => {
  const token = getAccessToken();

  console.log("createComment: ", postID, content);
    const response = await axios.post(`${apiUrl}/comment/create`, {
      postID,
      content,
    },{
      headers: {
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });
    return response.data;
};

export const upvotePost = async ({ postID }) => {
  const token = getAccessToken();
  try {
    const response = await axios.put(`${apiUrl}/post/upvote?postID=${postID}`, null, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });
    if (response.status !== 204) {
      throw new Error('Server Error');
    }
  } catch (error) {
    throw new Error(error.message);
  }
};

export const downvotePost = async ({ postID }) => {
  const token = getAccessToken();
  try {
    const response = await axios.put(`${apiUrl}/post/downvote?postID=${postID}`, null, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });
    if (response.status !== 204) {
      throw new Error('Server Error');
    }
  } catch (error) {
    throw new Error(error.message);
  }
};


// export const filterPosts = async ({ searchQuery, sortBy }) => {
//   try {
//     const response = await axios.get(`${apiUrl}/post/search}`, {
//       params: {
//         pattern: searchQuery,
//         sortBy: sortBy,
//       },
//     });
//     console.log("api works", response.data);
//     return response.data;
//   } catch (error) {
//     throw new Error(error.message);
//   }


// }

export const upvoteComment = async ({ postID, commentID }) => {
  const token = getAccessToken();

  try {
    const response = await axios.put(
      `${apiUrl}/comment/upvote?postID=${postID}&commentID=${commentID}`,
      {},
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token.credential}`,
          'X-Oauth-Provider': 'google',
          'X-Oauth-Credential': JSON.stringify(token.credential),
        },
      }
    );

    if (response.status !== 204) {
      throw new Error('Server Error');
    }

    return true;
  } catch (error) {
    throw new Error(error.message);
  }
};


export const downvoteComment = async ({ postID, commentID }) => {
  const token = getAccessToken();

  try {
    const response = await axios.put(`${apiUrl}/comment/downvote?postID=${postID}&commentID=${commentID}`, {}, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });
    if (response.status !== 204) {
      throw new Error('Server Error');
    }
  } catch (error) {
    throw new Error(error.message);
  }
};


export const deleteComment = async ({ postID, commentID }) => {
  const token = getAccessToken();
  try {
    const body = JSON.stringify({ postID, commentID });
    const response = await axios.delete(`${apiUrl}/comment/delete`, {
      params: {
        postID,
        commentID,
      }
        ,
      headers: {
        'Content-Type': 'application/json' ,
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      }
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const editComment = async ({ postID, commentID, content }) => {
  const token = getAccessToken();
  try {
    const response = await axios.put(`${apiUrl}/comment/edit`, JSON.stringify({ postID: postID, commentID: commentID, content: content }), {
      headers:
          {
            'Content-Type': 'application/json' ,
              Authorization: `Bearer ${token.credential}`,
              'X-Oauth-Provider': 'google',
              'X-Oauth-Credential': JSON.stringify(token.credential),
          },
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

// User related API functions
export const getUsers = async ({ userIDStart, count, reverse }) => {
  const token = getAccessToken();

  try {
    const response = await axios.get(`${apiUrl}/user/gets`, {
      params: {
        userIDStart,
        count,
        reverse,
      }, headers:
      {
        'Content-Type': 'application/json' ,
          Authorization: `Bearer ${token.credential}`,
          'X-Oauth-Provider': 'google',
          'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const getUser = async (userID) => {
  const token = getAccessToken();
  if (token === null || token === undefined) {
    try {
      const response = await axios.get(`${apiUrl}/user/get`, {
        params: { userID },
        headers:
        {
          'Content-Type': 'application/json' ,
        }
      });
      return response.data;
    } catch (error) {
      throw new Error(error.message);
    }
  } else {
    try {
      const response = await axios.get(`${apiUrl}/user/get`, {
        params: { userID },
        headers:
        {
          'Content-Type': 'application/json' ,
            Authorization: `Bearer ${token.credential}`,
            'X-Oauth-Provider': 'google',
            'X-Oauth-Credential': JSON.stringify(token.credential),
        },
      });
      return response.data;
    } catch (error) {
      throw new Error(error.message);
    }
  }
};


export const editUser = async ({ userID, name, email }) => {
  const token = getAccessToken();
  try {
    const response = await axios.put(`${apiUrl}/user/edit`, {
      userID,
      name,
      email,
    }, {
      headers: {
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const promoteUser = async ({ target, reason }) => {
  const token = getAccessToken();
  try {
    const response = await axios.put(`${apiUrl}/user/requestPromotion`, {
      target,
      reason,
      
    }, {
      headers: {
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

//admin related API functions
export const getPromotionRequests = async (requestIDStart, count, reverse) => {
  const token = getAccessToken();
  try {
    const response = await axios.get(`${apiUrl}/admin/gets`, {
      params: { requestIDStart, count, reverse },
      headers: {
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const promoteUserByAdmin = async (body) => {
  const token = getAccessToken();
  try {
    const response = await axios.put(`${apiUrl}/admin/promote`, body, {
      headers: {
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
        'Content-Type': 'application/json',
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const deletePromotionRequest = async (requestID) => {
  const token = getAccessToken();
  try {
    const response = await axios.delete(`${apiUrl}/admin/delete`, {
      params: { requestID },
      headers: {
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
        'Content-Type': 'application/json',
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const highestPostIndex = async() => {
  try {
    const response = await axios.get(`${apiUrl}/post/highest`);
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};