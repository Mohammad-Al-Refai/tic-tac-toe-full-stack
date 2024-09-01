export const AppTheme: IAppTheme = {
  colors: {
    primary: "#f9d459",
    onPrimary: "white",
    secondary: "#f22853",
    onSecondary: "white",
    background: "#101516",
    onBackground: "white",
    tertiary: "#035cc2",
    onTertiary: "white",
  },
  fontSize: {
    XS: "12px",
    S: "14px",
    M: "16px",
    L: "20px",
    XL: "24px",
    XL2: "34px",
    XL3: "48px",
  },
  XAxisSpacing: {
    XS: "2px",
    S: "4px",
    M: "8px",
    L: "12px",
    XL: "15px",
    XL2: "18px",
  },
  YAxisSpacing: {
    XS: "2px",
    S: "4px",
    M: "8px",
    L: "12px",
    XL: "15px",
    XL2: "18px",
  },
  Surroundings: {
    XS: "2px",
    S: "4px",
    M: "8px",
    L: "12px",
    XL: "15px",
    XL2: "18px",
  },
};

export interface IAppTheme {
  colors: {
    primary: string;
    onPrimary: string;
    secondary: string;
    onSecondary: string;
    background: string;
    onBackground: string;
    tertiary: string;
    onTertiary: string;
  };
  fontSize: {
    XS: string;
    S: string;
    M: string;
    L: string;
    XL: string;
    XL2: string;
    XL3: string;
  };
  XAxisSpacing: {
    XS: string;
    S: string;
    M: string;
    L: string;
    XL: string;
    XL2: string;
  };
  YAxisSpacing: {
    XS: string;
    S: string;
    M: string;
    L: string;
    XL: string;
    XL2: string;
  };
  Surroundings: {
    XS: string;
    S: string;
    M: string;
    L: string;
    XL: string;
    XL2: string;
  };
}
